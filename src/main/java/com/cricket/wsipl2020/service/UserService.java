package com.cricket.wsipl2020.service;
import com.cricket.wsipl2020.dto.BattingPoints;
import com.cricket.wsipl2020.dto.BowlingPoints;
import com.cricket.wsipl2020.dto.FieldingPoints;
import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.*;
import com.cricket.wsipl2020.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    AllocationsRepo allocationsRepo;

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
            System.out.println(LocalTime.now()+"---"+game.getGameTime()+"--- "+game.getGameTime().toLocalTime().minusMinutes(120));
            if((LocalTime.now().isBefore(game.getGameTime().toLocalTime().minusMinutes(60))))
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


        public void submitWinningTeam(Integer gameNum, String winningTeam, Double pointsEarned){
        String winnerExists = scheduleRepo.checkWinner(gameNum);
        if (winnerExists.equals("")){
            scheduleRepo.updateWinningTeam(gameNum, winningTeam, pointsEarned);
            List<String> userIds = predictionRepo.fetchWinningUsers(gameNum, winningTeam);
            predictionRepo.updatePoints(gameNum,userIds,pointsEarned);
            Integer updatecount = userRepo.updateUserPredictionPoints(userIds, pointsEarned);
        }
    }

    public List<PredictionPointsDTO> fetchPredictions() {
        List<PredictionPointsDTO> predList =  predictionRepo.fetchPredictionList();
        List<PredictionPointsDTO> finalList = new ArrayList<>();
        for(PredictionPointsDTO pred:predList){
            PredictionPointsDTO prediction = new PredictionPointsDTO();
            prediction.setGameNum(pred.getGameNum());
            prediction.setMaxPoints(pred.getMaxPoints());
            prediction.setPointsGained(pred.getPointsGained());
            prediction.setTeam1(pred.getTeam1());
            prediction.setTeam2(pred.getTeam2());
            prediction.setUserId(idToString(pred.getUserId()));
            prediction.setWinningTeam(pred.getWinningTeam());
            //System.out.println(LocalTime.now() + "Local time ccurrent");
            if((LocalTime.now().isBefore(LocalTime.of(18,30,0))))
            {
                prediction.setPrediction(null);

            }
            else{
                prediction.setPrediction(pred.getPrediction());
            }
            finalList.add(prediction);
        }
        return finalList;
    }

    public List<PredictionPointsDTO> fetchPredictionsByUser(String userId) {
        return predictionRepo.fetchPredictionsByUser(userId);
    }

    public List<Allocations> fetchAllocations(){
        return allocationsRepo.fetchAllocations();
    }


    //updates score in the database
    public void submitScore(Score playerScore){

        System.out.println("initial didwin  ismom"+playerScore.getIsMOM());
        //score repo me update
         scoreRepo.submitScore(playerScore.getScorePK().getGameNum(), playerScore.getScorePK().getPlayerName(),
                playerScore.getRunsScored(), playerScore.getBallsFaced(), playerScore.getFoursHit(), playerScore.getSixesHit(), playerScore.getIsNotout(),
                playerScore.getBallsBowled(), playerScore.getRunsConceded(), playerScore.getWicketsTaken(),playerScore.getDots(), playerScore.getBwldLbwCnb(), playerScore.getMaidenOvers(),playerScore.getHatricks(),
                playerScore.getCatchesTaken(), playerScore.getDirectHits(),playerScore.getFarDirectHits(), playerScore.getStumpings(),playerScore.getIsMOM(),playerScore.getDidWin());

        String role = playerRepo.getPlayerRole(playerScore.getScorePK().getPlayerName());
        Boolean isCaptain = playerRepo.checkIfCaptain(playerScore.getScorePK().getPlayerName());


        List<String> userIds = new ArrayList<>();
        userIds.addAll(playerScore.getMainUserIds());
        userIds.addAll(playerScore.getBackUpUserIds());
        userIds.addAll(playerScore.getRivalBackUpUserIds());
        System.out.println(userIds.size());

                DailyPlayerPoints dailyPlayerPoints = calculateTotalPoints(playerScore.getRunsScored(), playerScore.getBallsFaced(), playerScore.getFoursHit(), playerScore.getSixesHit(), playerScore.getIsNotout(),
                 playerScore.getBallsBowled(), playerScore.getRunsConceded(), playerScore.getWicketsTaken(),playerScore.getBwldLbwCnb(), playerScore.getMaidenOvers(),playerScore.getHatricks(),playerScore.getDots(),
                 playerScore.getCatchesTaken(), playerScore.getDirectHits(), playerScore.getFarDirectHits(), playerScore.getStumpings(), role,userIds, isCaptain, playerScore.getIsMOM(),playerScore.getDidWin());

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
        userRepo.updateDailyPoints(dailyPlayerPoints.getTotalGamePoints(),playerScore.getMainUserIds());
        if(null!=playerScore.getBackUpUserIds()){
            userRepo.updateDailyPoints(dailyPlayerPoints.getTotalGamePoints()*0.90,playerScore.getBackUpUserIds());
        }
        if(null!=playerScore.getRivalBackUpUserIds()){
            userRepo.updateDailyPoints(dailyPlayerPoints.getTotalGamePoints()*0.50,playerScore.getRivalBackUpUserIds());
        }

        if(playerScore.getMainUserIds()!=null){
            for(String user : playerScore.getMainUserIds()){
                String id = idToString(user);
                System.out.println(user);
                allocationsRepo.addAllocation(playerScore.getScorePK().getGameNum(),id,playerScore.getScorePK().getPlayerName(),dailyPlayerPoints.getTotalGamePoints());
            }
        }
        if(playerScore.getBackUpUserIds()!=null){
            for(String user : playerScore.getBackUpUserIds()){
                String id = idToString(user);
                System.out.println(user);
                allocationsRepo.addAllocation(playerScore.getScorePK().getGameNum(),id,playerScore.getScorePK().getPlayerName(),dailyPlayerPoints.getTotalGamePoints()*0.90);
            }
        }
        if(playerScore.getRivalBackUpUserIds()!=null){
            for(String user : playerScore.getRivalBackUpUserIds()){
                String id = idToString(user);
                System.out.println(user);
                allocationsRepo.addAllocation(playerScore.getScorePK().getGameNum(),id,playerScore.getScorePK().getPlayerName(),dailyPlayerPoints.getTotalGamePoints()*0.50);
            }
        }
        String totalPoints = "("+dailyPlayerPoints.getTotalGamePoints().toString()+")";
        String score =","+ playerScore.getScorePK().getGameNum().toString().concat(totalPoints);
        playerRepo.updatePlayerScore(playerScore.getScorePK().getPlayerName(), dailyPlayerPoints.getTotalGamePoints(),score);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public DailyPlayerPoints calculateTotalPoints(Integer runsScored, Integer balls, Integer fours, Integer sixes, Boolean isNO,
                                                  Integer ballsBowled, Integer runsGiven, Integer wickets, Integer bwldLb, Integer maidens,Integer hatrick, Integer dots,
                                                  Integer catches, Integer directHits, Integer farDirectHits, Integer stumpings, String role, List<String> userIds,
                                                  Boolean isCaptain, Boolean isMOM, Boolean didWin){

        DailyPlayerPoints dailyPlayerPoints = new DailyPlayerPoints();

        BattingPoints finalBattingPoints = calculateBattingPoints( runsScored,  balls,  fours,  sixes,  isNO, role);
        BowlingPoints finalBowlingPoints = calculateBowlingPoints(ballsBowled,  runsGiven,  wickets,  bwldLb,  maidens, hatrick, dots);
        FieldingPoints finalFieldingPoints = calculateFieldingPoints(catches,  directHits,farDirectHits, stumpings);

        dailyPlayerPoints.setRunsScored(finalBattingPoints.getRunsScored());
        dailyPlayerPoints.setBallsFaced(finalBattingPoints.getBallsFaced());
        dailyPlayerPoints.setRunsPoints(finalBattingPoints.getRunsPoints());
        dailyPlayerPoints.setFoursPoints(finalBattingPoints.getFoursPoints());
        dailyPlayerPoints.setSixesPoints(finalBattingPoints.getSixesPoints());
        dailyPlayerPoints.setWasNO(finalBattingPoints.getWasNO());
        dailyPlayerPoints.setRunsBonus(finalBattingPoints.getRunsBonus());
        dailyPlayerPoints.setStrikeRate(finalBattingPoints.getStrikeRate());
        dailyPlayerPoints.setStrikeRateBonus(finalBattingPoints.getStrikeRateBonus());
        dailyPlayerPoints.setBattingPoints(finalBattingPoints.getBattingPoints());

        dailyPlayerPoints.setBallsBowled(finalBowlingPoints.getBallsBowled());
        dailyPlayerPoints.setRunsConceded(finalBowlingPoints.getRunsConceded());
        dailyPlayerPoints.setWicketPoints(finalBowlingPoints.getWicketPoints());
        dailyPlayerPoints.setEconomyBonus(finalBowlingPoints.getEconomyBonus());
        dailyPlayerPoints.setEconomy(finalBowlingPoints.getEconomy());
        dailyPlayerPoints.setHatrickBonus(finalBowlingPoints.getHatrickBonus());
        dailyPlayerPoints.setMaidenOverBonus(finalBowlingPoints.getMaidenOverBonus());
        dailyPlayerPoints.setLbwOrBldPoints(finalBowlingPoints.getLbwOrBldPoints());
        dailyPlayerPoints.setWicketsTaken(finalBowlingPoints.getWicketsTaken());
        dailyPlayerPoints.setDotsPints(finalBowlingPoints.getDotsPoints());
        dailyPlayerPoints.setBowlingPoints(finalBowlingPoints.getBowlingPoints());

        dailyPlayerPoints.setCatchesPoints(finalFieldingPoints.getCatchesPoints());
        dailyPlayerPoints.setStumpingPoints(finalFieldingPoints.getStumpingPoints());
        dailyPlayerPoints.setDirectHitPoints(finalFieldingPoints.getDirectHitPoints());
        dailyPlayerPoints.setFieldingPoints(finalFieldingPoints.getFieldingPoints());
        dailyPlayerPoints.setMomOrWinPoints(0);

        System.out.println("IS MOM ?"+isMOM);
        System.out.println("DIDwin ?"+didWin);
        if(isMOM == true){
            System.out.println("inside MOOM");
            dailyPlayerPoints.setMomOrWinPoints(10);
        }


        dailyPlayerPoints.setTotalGamePoints(dailyPlayerPoints.getBattingPoints()+dailyPlayerPoints.getBowlingPoints()+dailyPlayerPoints.getFieldingPoints()+dailyPlayerPoints.getMomOrWinPoints());
        if(isCaptain == true){
            System.out.println("inside captaincy");

            dailyPlayerPoints.setTotalGamePoints(dailyPlayerPoints.getTotalGamePoints()*1.5);

            if(didWin == true){
                dailyPlayerPoints.setTotalGamePoints(dailyPlayerPoints.getTotalGamePoints()+10);
            }
            else{
                dailyPlayerPoints.setTotalGamePoints(dailyPlayerPoints.getTotalGamePoints()-5);
            }
        }
        dailyPlayerPoints.setAssignedTo(idsToString(userIds));
        return dailyPlayerPoints;

    }

    private static BattingPoints calculateBattingPoints(Integer runsScored, Integer balls, Integer fours, Integer sixes, Boolean isNO, String role){

        BattingPoints battingPoints = new BattingPoints();

        Double runsPoints = 0.0;
        Integer runsBonus = 0;
        Double foursPoints = fours * 0.5;
        Integer sixesPoints = sixes * 1;
        Double totalBattingPoints ;
        Double strikeRate = 0.0;
        Integer strikeRateBonus = 0;

        //out on 0
        if(runsScored == 0 && isNO == false) {
            System.out.println("ROLE:" +role);
            if(balls > 0)
            {
                if(role.equals("Bowler") || role.equals("Spinner")){
                    System.out.println("ROLE:" +role);
                    runsPoints = runsPoints - 2;
                }
                else{
                 runsPoints = runsPoints - 5;
                }
            }
        }
        else if (runsScored > 0){
            runsPoints = runsScored * 0.5;
            strikeRate = (runsScored * 100.0)/ balls;
            strikeRate = round(strikeRate,3);

            if (balls >= 15 || runsScored >=30){
                if(strikeRate >= 200){
                    if(balls >= 45){
                        strikeRateBonus = strikeRateBonus + 15;
                    }
                    else if(balls >= 25){
                        strikeRateBonus = strikeRateBonus + 11;
                    }
                    else {
                        strikeRateBonus = strikeRateBonus + 8;
                    }
                }
                else if(strikeRate >= 175.0 && strikeRate < 200.0){
                    strikeRateBonus = strikeRateBonus + 7;
                }
                else if(strikeRate >= 150.0 && strikeRate < 175.0){
                    strikeRateBonus = strikeRateBonus + 5;
                }
                else if(strikeRate >= 120.0 && strikeRate < 150.0){
                    strikeRateBonus = strikeRateBonus +3;
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
            runsBonus = runsBonus + 14;
        }
        else if(runsScored >= 50 && runsScored <100){
            runsBonus = runsBonus + 6;
        }
        else if(runsScored >= 30 && runsScored < 50){
            runsBonus = runsBonus + 3;
        }

        if(isNO == true && runsScored >= 25)
        {
            runsBonus = runsBonus + 5;
        }

        totalBattingPoints = runsPoints + foursPoints + sixesPoints + runsBonus+ strikeRateBonus;
        battingPoints.setRunsScored(runsScored);
        battingPoints.setBallsFaced(balls);
        battingPoints.setRunsPoints(runsPoints);
        battingPoints.setFoursPoints(foursPoints);
        battingPoints.setSixesPoints(sixesPoints);
        battingPoints.setWasNO(isNO == true ? "YES" : "NO");
        battingPoints.setRunsBonus(runsBonus);
        battingPoints.setStrikeRate(strikeRate);
        battingPoints.setStrikeRateBonus(strikeRateBonus);
        battingPoints.setBattingPoints(totalBattingPoints);
        return battingPoints;
    }

    private static BowlingPoints calculateBowlingPoints(Integer ballsBowled, Integer runsGiven, Integer wickets, Integer bwldLb, Integer maidens, Integer hatrick, Integer dots) {

        BowlingPoints bowlingPoints = new BowlingPoints();
        Integer wicketPoints = 0;
        Integer economyBonus = 0;
        Integer hatrickBonus =0;
        Integer maidenOverBonus =0;
        Integer lbwOrBldPoints=0;
        Double economy = 0.0;
        Double totalBowlingPoints = 0.0;
        Double dotsBonus = dots*0.75;
        economy = ((runsGiven * 6.0) / ballsBowled);

        if(ballsBowled > 0) {
            if(ballsBowled >=10){
                if(economy<=4){
                    wicketPoints = wickets * 15;
                }
                else if(economy >4 && economy <=5){
                    wicketPoints = wickets * 14;
                }
                else if(economy >5 && economy <=6){
                    wicketPoints = wickets * 13;
                }
                else if(economy >6 && economy <=7.50){
                    wicketPoints = wickets * 12;
                }
                else if(economy >7.50 && economy <=9.0){
                    wicketPoints = wickets * 11;
                }
                else if(economy >9.0 && economy <=13.0){
                    wicketPoints = wickets * 10;
                }
                else {
                    wicketPoints = wickets * 8;
                }
            }
            else{
                wicketPoints = wickets * 12;
            }

            lbwOrBldPoints = bwldLb * 5;

            if (wickets >= 3 && wickets < 5) {
                wicketPoints = wicketPoints + 7;
            } else if (wickets == 5) {
                wicketPoints = wicketPoints + 10;
            } else if (wickets > 5) {
                wicketPoints = wicketPoints + 12;
            }
            hatrickBonus = hatrick * 10;
            maidenOverBonus = maidens * 6;

            economyBonus = 0;
            if (ballsBowled >= 10) {
                if (economy > 5 && economy <= 6) {
                    economyBonus = economyBonus + 8;
                } else if (economy > 4 && economy <= 5) {
                    economyBonus = economyBonus + 10;
                } else if (economy <= 4) {
                    economyBonus = economyBonus + 15;
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
        totalBowlingPoints =totalBowlingPoints + wicketPoints + economyBonus + hatrickBonus + maidenOverBonus + lbwOrBldPoints + dotsBonus;
        bowlingPoints.setBallsBowled(ballsBowled);
        bowlingPoints.setRunsConceded(runsGiven);
        bowlingPoints.setWicketPoints(wicketPoints);
        bowlingPoints.setEconomyBonus(economyBonus);
        bowlingPoints.setEconomy(economy);
        bowlingPoints.setDotsPoints(dotsBonus);
        bowlingPoints.setHatrickBonus(hatrickBonus);
        bowlingPoints.setMaidenOverBonus(maidenOverBonus);
        bowlingPoints.setLbwOrBldPoints(lbwOrBldPoints);
        bowlingPoints.setWicketsTaken(wickets);
        bowlingPoints.setBowlingPoints(totalBowlingPoints);
        return bowlingPoints;

    }

    private static FieldingPoints calculateFieldingPoints(Integer catches, Integer directHits,Integer farDirectHits, Integer stumpings) {
        Integer catchesPoints = catches * 5;
        Integer stumpingPoints = stumpings * 7;
        Integer directHitPoints = (directHits * 5)+(farDirectHits*12);

        FieldingPoints fieldingPoints = new FieldingPoints();
        fieldingPoints.setCatchesPoints(catchesPoints);
        fieldingPoints.setStumpingPoints(stumpingPoints);
        fieldingPoints.setDirectHitPoints(directHitPoints);
        fieldingPoints.setFieldingPoints(catchesPoints + stumpingPoints + directHitPoints);
        return fieldingPoints;
    }

    public List<Allocations> fetchAllocationsByName(String user_id) {
        List<Allocations> allocationByName = allocationsRepo.fetchAllocationsByName(user_id);
        return allocationByName;
    }
    private static String idsToString(List<String> userIds){

        String out = "";
        for(String userId: userIds){
            if (userId.equals("Aakashdhoot.ad@gmail.com")){
                out = out +" -"+ "Aksh" ;
            } else if (userId.equals( "Deepak.kotwani29@gmail.com")){
                out = out +" -"+ "Dpu";
            }else if (userId.equals("devpandya_0072@yahoo.in")){
                out = out + " -"+"Dvng";
            }else if (userId.equals("dhruvikp27@gmail.com")){
                out = out + " -"+"Dhrvk";
            }else if (userId.equals("harsh242bme@gmail.com")){
                out = out + " -"+"Lkhn";
            }else if (userId.equals("lmcp.sachin@gmail.com")){
                out = out + " -"+"Schn";
            }else if (userId.equals("ksrajput@asu.edu")){
                out = out +" -"+ "Kshv";
            }else if (userId.equals("photowalktd@icloud.com")){
                out = out +" -"+ "Tnmy";
            }else if (userId.equals("prthtalele1@gmail.com")){
                out = out + " -"+"Prth";
            }else if (userId.equals("sagarpatel2804@gmail.com")){
                out = out +" -"+ "Sgr";
            }else if (userId.equals("Sid.pandya8@gmail.com")){
                out = out +" -"+ "Sid";
            }else if (userId.equals("swapnilelec@gmail.com")){
                out = out +" -"+ "Swpnl";
            } else if (userId.equals("ved.dave24@gmail.com")){
                out = out +" -"+ "Ved";
            }else if (userId.equals("keyur.dittanwala@gmail.com")){
                out = out +" -"+ "Kyur";
            }
            else{
                out = out+"";
            }
        }
        return out;
    }

    private static String idToString(String userId){


            if (userId.equals("Aakashdhoot.ad@gmail.com")){
                return "Akash" ;
            }
            else if (userId.equals( "Deepak.kotwani29@gmail.com")){
                return "Deepu";
            }else if (userId.equals("devpandya_0072@yahoo.in")){
                return "Devang";
            }else if (userId.equals("dhruvikp27@gmail.com")){
                return "Dhruvik";
            }else if (userId.equals("harsh242bme@gmail.com")){
                return "Lakhan";
            }else if (userId.equals("lmcp.sachin@gmail.com")){
                return "Sachin";
            }else if (userId.equals("ksrajput@asu.edu")){
                return "Keshav";
            }else if (userId.equals("photowalktd@icloud.com")){
                return "Tanmay";
            }else if (userId.equals("prthtalele1@gmail.com")){
                return "Parth";
            }else if (userId.equals("sagarpatel2804@gmail.com")){
                return "Sagar";
            }else if (userId.equals("Sid.pandya8@gmail.com")){
                return "Sid";
            }else if (userId.equals("swapnilelec@gmail.com")){
                return "Swapnil";
            }else if (userId.equals("test@abc.com")){
                return "Sample";
            }
            else if (userId.equals("test@123.com")){
                return "Test123";
            }
            else if (userId.equals("test@user.com")){
                return "TestUser";
            }
            else if (userId.equals("ved.dave24@gmail.com")){
                return"Ved";
            }else if (userId.equals("keyur.dittanwala@gmail.com")){
                return "Keyur";
            }else{
                return "";
            }
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
