package com.cricket.wsipl2020.service;
import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.*;
import com.cricket.wsipl2020.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ScheduleRepo scheduleRepo;

    @Autowired
    PredictionRepo predictionRepo;

    @Autowired
    ScoreRepo scoreRepo;

    @Autowired
    DailyPlayerPointsRepo dailyPlayerPointsRepo;

    @Autowired
    PlayerRepo playerRepo;
//    public boolean authenticate(String userName, String pwd) {
//        User user =  userRepo.getUser(userName);
//        if (user.getPwd().equals(pwd)) {
//            return true;
//        }
//        else {
//            return false;
//        }
//    }

    public List<Schedule> fetchSchedule(){
        return scheduleRepo.getSchedule();
    }

    public List<Schedule> fetchTodaysGame(){

        List<Schedule> predictionEligibleGames =  new ArrayList<>();

        System.out.println(LocalDate.now());
        System.out.println(LocalDateTime.now());
        List<Schedule> gamesToday = scheduleRepo.getGameByDate(LocalDate.now());

        for(Schedule game : gamesToday) {
            System.out.println(LocalTime.now()+"--- "+game.getGameTime().toLocalTime().minusMinutes(120));
            if((LocalTime.now().isBefore(game.getGameTime().toLocalTime().minusMinutes(90))))
            {
                predictionEligibleGames.add(game);
            }
        }
        return predictionEligibleGames;


    }

    public void submitPrediction(Integer gameId, String userId, String predictedTeam) {
        Integer predicitonExists = predictionRepo.checkPrediction(gameId, userId);

        if(predicitonExists == 0) {
            //insert into prediction
            predictionRepo.submitPrediction(gameId,userId,predictedTeam);
        }
        else{
            //update predition as the predicction already exists
            predictionRepo.updatePredition(gameId,userId,predictedTeam);
        }
    }

    public List<User> fetchAllUsers()
    {
        List<User> userList = (List<User>)userRepo.findAll();
        return userList;
    }

    public List<Score> fetchDailyScores(){
        return scoreRepo.fetchScores();
    }

    public List<DailyPlayerPoints> fetchDailyFantasyPoints(){
        //return (List<DailyPlayerPoints>) dailyPlayerPointsRepo.findAll();
        return dailyPlayerPointsRepo.fetchDailyPlayerPoints();
    }

    public List<Player> fetchPlayers(){
        return  playerRepo.fetchPlayers();
    }
    public List<PointsTableResponse> fetchPredictionPointsTable() {
        return userRepo.fetchPredictionPointsTable();
    }

//    public List<PointsTableResponse> fetchMyPrediccitonPoints() {
//        return userRepo.fetchPredictionPointsTable();
//    }

    public List<PointsTableResponse> fetchFantasyPointsTable() {
        return userRepo.fetchFantasyPointsTable();
    }
        public List<User> fetchCompleteUserDetails(String userId){
        return userRepo.fetchCompleteUserDetails(userId);
    }


        public void submitWinningTeam(Integer gameNum, String winningTeam, Float pointsEarned){
        String winnerExists = scheduleRepo.checkWinner(gameNum);

        if (winnerExists == null){
            scheduleRepo.updateWinningTeam(gameNum, winningTeam, pointsEarned);
            List<String> userIds = predictionRepo.fetchWinningUsers(gameNum, winningTeam);
            predictionRepo.updatePoints(gameNum,userIds,pointsEarned);
            Integer updatecount = userRepo.updateUserPredictionPoints(userIds, pointsEarned);
        }


    }

    public List<PredictionPointsDTO> fetchPredictions() {
        return predictionRepo.fetchPredictionList();
    }

    public List<PredictionPointsDTO> fetchPredictionsByUser(String userId) {
        return predictionRepo.fetchPredictionsByUser(userId);
    }

    public void submitScore(Score playerScore){

         scoreRepo.submitScore(playerScore.getScorePK().getGameNum(), playerScore.getScorePK().getPlayerName(),
                playerScore.getRunsScored(), playerScore.getBallsFaced(), playerScore.getFoursHit(), playerScore.getSixesHit(), playerScore.getIsNotout(),
                playerScore.getBallsBowled(), playerScore.getRunsConceded(), playerScore.getWicketsTaken(), playerScore.getBwldLbwCnb(), playerScore.getMaidenOvers(),playerScore.getHatricks(),
                playerScore.getCatchesTaken(), playerScore.getDirectHits(), playerScore.getStumpings());

        String role = playerRepo.getPlayerRole(playerScore.getScorePK().getPlayerName());
        Boolean isCaptain = playerRepo.checkIfCaptain(playerScore.getScorePK().getPlayerName());



                DailyPlayerPoints dailyPlayerPoints = calculateTotalPoints(playerScore.getRunsScored(), playerScore.getBallsFaced(), playerScore.getFoursHit(), playerScore.getSixesHit(), playerScore.getIsNotout(),
                 playerScore.getBallsBowled(), playerScore.getRunsConceded(), playerScore.getWicketsTaken(),playerScore.getBwldLbwCnb(), playerScore.getMaidenOvers(),playerScore.getHatricks(),
                 playerScore.getCatchesTaken(), playerScore.getDirectHits(), playerScore.getStumpings(), role,playerScore.getUserIds(), isCaptain);

        DailyPlayerPointsPK dailyPlayerPointsPK = new DailyPlayerPointsPK( playerScore.getScorePK().getGameNum(),playerScore.getScorePK().getPlayerName());
        dailyPlayerPoints.setDailyPlayerPointsPK(dailyPlayerPointsPK);


         dailyPlayerPointsRepo.save(dailyPlayerPoints);

         //fetch all users.userName where powerplayer = ""
        // playername - checck users with thihs player as power player -keshav
       // List<String> ppUserIds = playerRepo.fetchPowerPlayer(playerScore.getScorePK().getPlayerName());
        List<String> userIdsPP = userRepo.fetchUserIdsPP(playerScore.getScorePK().getPlayerName());



        if(userIdsPP!=null){
            userRepo.updateDailyPoints(dailyPlayerPoints.getTotalGamePoints(),userIdsPP);

        }

         userRepo.updateDailyPoints(dailyPlayerPoints.getTotalGamePoints(),playerScore.getUserIds());
        String totalPoints = "("+dailyPlayerPoints.getTotalGamePoints().toString()+")";
        String score =","+ playerScore.getScorePK().getGameNum().toString().concat(totalPoints);

         playerRepo.updatePlayerScore(playerScore.getScorePK().getPlayerName(), dailyPlayerPoints.getTotalGamePoints(),score);



    }

    public DailyPlayerPoints calculateTotalPoints(Integer runsScored, Integer balls, Integer fours, Integer sixes, Boolean isNO,
                                                  Integer ballsBowled, Integer runsGiven, Integer wickets, Integer bwldLb, Integer maidens,Integer hatrick,
                                                  Integer catches, Integer directHits, Integer stumpings, String role, List<String> userIds, Boolean isCaptain){

        DailyPlayerPoints dailyPlayerPoints = new DailyPlayerPoints();
        Float runsPoints = 0.0f;
        Integer runsBonus = 0;
        Float foursPoints = fours * 0.5f;
        Integer sixesPoints = sixes * 1;
        Float battingPoints = 0.0f;
        Integer bowlingPoints = 0;
        Integer fieldingPoints = 0;
        Float strikeRate = 0.0f;
        Integer strikeRateBonus = 0;
        Integer wicketPoints = 0;
                Integer economyBonus = 0;
                Integer hatrickBonus =0;
                Integer maidenOverBonus =0;
                Integer lbwOrBldPoints=0;
        Float economy = 0.0f;


        if(runsScored == 0 && isNO == false) {
            System.out.println("ROLE:" +role);
            if(role.equals("Fast-Bowler") || role.equals("Spinner")){
                System.out.println("ROLE:" +role);
                runsPoints = runsPoints - 2;
            }
            else{
            runsPoints = runsPoints - 5;
            }

        }
        else if (runsScored > 0){
            runsPoints = runsScored * 0.5f;
            strikeRate = (runsScored * 100.0f )/ balls;

            if (balls >= 15){
                if(strikeRate >200){
                    if(balls >= 45){
                        strikeRateBonus = strikeRateBonus + 10;
                    }
                    else if(balls >= 25){
                        strikeRateBonus = strikeRateBonus + 7;
                    }
                    else {
                        strikeRateBonus = strikeRateBonus + 5;
                    }
                }
                else if(strikeRate >= 175.0 && strikeRate < 200.0){
                    strikeRateBonus = strikeRateBonus + 4;
                }
                else if(strikeRate >= 150.0 && strikeRate < 175.0){
                    strikeRateBonus = strikeRateBonus + 3;
                }
                else if(strikeRate >= 120.0 && strikeRate < 150.0){
                    strikeRateBonus = strikeRateBonus + 2;
                }
                else if(strikeRate >= 60.0 && strikeRate < 70){
                    strikeRateBonus = strikeRateBonus - 2;
                }
                else if(strikeRate >= 50.0 && strikeRate < 60){
                    strikeRateBonus = strikeRateBonus - 3;
                }
                else if(strikeRate < 50){
                    strikeRateBonus = strikeRateBonus - 5;
                }
            }
        }

        if(runsScored >= 100){
            runsBonus = runsBonus + 10;
        }
        else if(runsScored >= 50 && runsScored <100){
            runsBonus = runsBonus + 5;
        }
        else if(runsScored >= 30 && runsScored < 50){
            runsBonus = runsBonus + 3;
        }

        if(isNO == true && runsScored >= 15)
        {
            runsBonus = runsBonus + 7;
        }


        if(ballsBowled > 0) {

             wicketPoints = wickets * 10;
             lbwOrBldPoints = bwldLb * 4;


            if (wickets >= 3 && wickets < 5) {
                wicketPoints = wicketPoints + 4;
            } else if (wickets == 5) {
                wicketPoints = wicketPoints + 8;
            } else if (wickets > 5) {
                wicketPoints = wicketPoints + 10;
            }

             hatrickBonus = hatrick * 10;
             maidenOverBonus = maidens * 3;

             economy = ((runsGiven * 6.0f) / ballsBowled);
             economyBonus = 0;
            if (ballsBowled >= 12) {
                if (economy >= 5 && economy < 6) {
                    economyBonus = economyBonus + 2;
                } else if (economy >= 4 && economy < 5) {
                    economyBonus = economyBonus + 3;
                } else if (economy < 4) {
                    economyBonus = economyBonus + 5;
                } else if (economy >= 9 && economy < 10) {
                    economyBonus = economyBonus - 2;
                } else if (economy >= 10 && economy < 11) {
                    economyBonus = economyBonus - 3;
                } else if (economy >= 11 && economy < 13) {
                    economyBonus = economyBonus - 5;
                } else if (economy >= 13 && economy < 15) {
                    economyBonus = economyBonus - 8;
                } else if (economy >= 15) {
                    economyBonus = economyBonus - 10;
                }
            }
        }

        Integer catchesPoints = catches * 4;
        Integer stumpingPoints = stumpings * 6;
        Integer directHitPoints = directHits * 10;

        battingPoints = runsPoints + foursPoints + sixesPoints + runsBonus+ strikeRateBonus;
        bowlingPoints = wicketPoints + economyBonus + hatrickBonus + maidenOverBonus + lbwOrBldPoints;
        fieldingPoints = catchesPoints + stumpingPoints + directHitPoints;
        Float totalGamePoints = battingPoints + bowlingPoints + fieldingPoints;
        System.out.println(isCaptain);
        if(isCaptain == true){
            System.out.println("inside captaincy");
            totalGamePoints = totalGamePoints * 1.5F;
        }

        dailyPlayerPoints.setRunsScored(runsScored);
        dailyPlayerPoints.setBallsFaced(balls);
        dailyPlayerPoints.setRunsPoints(runsPoints);
        dailyPlayerPoints.setFoursPoints(foursPoints);
        dailyPlayerPoints.setSixesPoints(sixesPoints);
        dailyPlayerPoints.setWasNO(isNO == true ? "YES" : "NO");
        dailyPlayerPoints.setRunsBonus(runsBonus);
        dailyPlayerPoints.setStrikeRate(strikeRate);
        dailyPlayerPoints.setStrikeRateBonus(strikeRateBonus);
        dailyPlayerPoints.setBattingPoints(battingPoints);

        dailyPlayerPoints.setBallsBowled(ballsBowled);
        dailyPlayerPoints.setRunsConceded(runsGiven);
        dailyPlayerPoints.setWicketPoints(wicketPoints);
        dailyPlayerPoints.setEconomyBonus(economyBonus);
        dailyPlayerPoints.setEconomy(economy);
        dailyPlayerPoints.setHatrickBonus(hatrickBonus);
        dailyPlayerPoints.setMaidenOverBonus(maidenOverBonus);
        dailyPlayerPoints.setLbwOrBldPoints(lbwOrBldPoints);
        dailyPlayerPoints.setWicketsTaken(wickets);
        dailyPlayerPoints.setBowlingPoints(bowlingPoints);

        dailyPlayerPoints.setCatchesPoints(catchesPoints);
        dailyPlayerPoints.setStumpingPoints(stumpingPoints);
        dailyPlayerPoints.setDirectHitPoints(directHitPoints);
        dailyPlayerPoints.setFieldingPoints(fieldingPoints);

        dailyPlayerPoints.setTotalGamePoints(totalGamePoints);
        dailyPlayerPoints.setAssignedTo(idsToString(userIds));

        return dailyPlayerPoints;

    }


    private static String idsToString(List<String> userIds){

        String out = "";
        for(String userId: userIds){
            if (userId.equals("Aakashdhoot.ad@gmail.com")){
                out = out +" -"+ "Aksh" ;
            }
            else if (userId.equals( "Deepak.kotwani29@gmail.com")){
                out = out +" -"+ "Dipu";
            }else if (userId.equals("devpandya_0072@yahoo.in")){
                out = out + " -"+"Devng";
            }else if (userId.equals("dhruvikp27@gmail.com")){
                out = out + " -"+"Dhrvk";
            }else if (userId.equals("harsh242bme@gmail.com")){
                out = out + " -"+"Lakhn";
            }else if (userId.equals("lmcp.sachin@gmail.com")){
                out = out + " -"+"Sachn";
            }else if (userId.equals("ksrajput@asu.edu")){
                out = out +" -"+ "Kshav";
            }else if (userId.equals("photowalktd@icloud.com")){
                out = out +" -"+ "Tnmy";
            }else if (userId.equals("prthtalele1@gmail.com")){
                out = out + " -"+"Prth";
            }else if (userId.equals("sagarpatel2804@gmail.com")){
                out = out +" -"+ "Sagr";
            }else if (userId.equals("Sid.pandya8@gmail.com")){
                out = out +" -"+ "Sid";
            }else if (userId.equals("swapnilelec@gmail.com")){
                out = out +" -"+ "Swpnl";
            }else{
                out = out+"";
            }




        }

        return out;
    }
//
//    public static void main(String[] args){
//        List<String> ids = new ArrayList<>();
//        ids.add("photowalktd@icloud.com");
//        ids.add("swapnilelec@gmail.com");
//        ids.add("tanmas@kiasda.com");
//
//        System.out.println("response: "+idsToString(ids));
//
//
//    }



}
