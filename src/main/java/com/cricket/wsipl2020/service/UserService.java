package com.cricket.wsipl2020.service;
import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.*;
import com.cricket.wsipl2020.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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

        System.out.print(LocalDate.now());
        List<Schedule> gamesToday = scheduleRepo.getGameByDate(LocalDate.now());

        for(Schedule game : gamesToday) {
            System.out.println(game.getGameTime()+"  "+LocalTime.now().plusMinutes(90));
            if((LocalTime.now().plusMinutes(90).isBefore(game.getGameTime().toLocalTime())))
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

    public void submitWinningTeam(Integer gameNum, String winningTeam, Float pointsEarned){
        String winnerExists = scheduleRepo.checkWinner(gameNum);

        if (winnerExists == null){
            scheduleRepo.updateWinningTeam(gameNum, winningTeam, pointsEarned);
            List<String> userIds = predictionRepo.fetchWinningUsers(gameNum, winningTeam);
            predictionRepo.updatePoints(gameNum,userIds,pointsEarned);
            Integer updatecount = userRepo.updateUserPredictionPoints(userIds, pointsEarned);
        }


    }

    public List<PredictionPointsDTO> fetchPredictions(String userId) {
        return predictionRepo.fetchPredictionList(userId);
    }

    public void submitScore(Score playerScore){

         scoreRepo.submitScore(playerScore.getScorePK().getGameNum(), playerScore.getScorePK().getPlayerName(),
                playerScore.getRunsScored(), playerScore.getBallsFaced(), playerScore.getFoursHit(), playerScore.getSixesHit(), playerScore.getIsNotout(),
                playerScore.getBallsBowled(), playerScore.getRunsConceded(), playerScore.getWicketsTaken(), playerScore.getBwldLbwCnb(), playerScore.getMaidenOvers(),playerScore.getHatricks(),
                playerScore.getCatchesTaken(), playerScore.getDirectHits(), playerScore.getStumpings());

         DailyPlayerPoints dailyPlayerPoints = calculateTotalPoints(playerScore.getRunsScored(), playerScore.getBallsFaced(), playerScore.getFoursHit(), playerScore.getSixesHit(), playerScore.getIsNotout(),
                 playerScore.getBallsBowled(), playerScore.getRunsConceded(), playerScore.getWicketsTaken(),playerScore.getBwldLbwCnb(), playerScore.getMaidenOvers(),playerScore.getHatricks(),
                 playerScore.getCatchesTaken(), playerScore.getDirectHits(), playerScore.getStumpings());

        DailyPlayerPointsPK dailyPlayerPointsPK = new DailyPlayerPointsPK( playerScore.getScorePK().getGameNum(),playerScore.getScorePK().getPlayerName());
        dailyPlayerPoints.setDailyPlayerPointsPK(dailyPlayerPointsPK);

         dailyPlayerPointsRepo.save(dailyPlayerPoints);

         userRepo.updateDailyPoints(dailyPlayerPoints.getTotalGamePoints(),playerScore.getUserIds());
         playerRepo.updatePlayerScore(playerScore.getScorePK().getPlayerName(), dailyPlayerPoints.getTotalGamePoints());



    }

    public DailyPlayerPoints calculateTotalPoints(Integer runsScored, Integer balls, Integer fours, Integer sixes, Boolean isNO,
                                                  Integer ballsBowled, Integer runsGiven, Integer wickets, Integer bwldLb, Integer maidens,Integer hatrick,
                                                  Integer catches, Integer directHits, Integer stumpings){

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


        if(runsScored == 0 && isNO == false) {
            runsPoints = runsPoints - 5;

        }
        else{
            runsPoints = runsScored * 0.5f;
            strikeRate = (runsScored * 100.0f )/ balls;

            if (balls > 15){
                if(strikeRate >200){
                    if(balls > 45){
                        strikeRateBonus = strikeRateBonus + 10;
                    }
                    else if(balls > 25){
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

        if(runsScored > 100){
            runsBonus = runsBonus + 10;
        }
        else if(runsScored > 50){
            runsBonus = runsBonus + 5;
        }
        else if(runsScored > 30){
            runsBonus = runsBonus + 3;
        }

        if(isNO == true && runsScored > 15)
        {
            runsBonus = runsBonus + 7;
        }



        Integer wicketPoints = wickets * 10;
        Integer lbwOrBldPoints = bwldLb * 4;


        if(wickets >= 3 && wickets < 5){
            wicketPoints = wicketPoints + 4;
        }
        else if(wickets == 5){
            wicketPoints = wicketPoints + 8;
        }
        else if (wickets > 5){
            wicketPoints = wicketPoints + 10;
        }

        Integer hatrickBonus = hatrick * 10;
        Integer maidenOverBonus = maidens * 3;

        Float economy = ((runsGiven * 6.0f) /ballsBowled);
        Integer economyBonus = 0;
        if(ballsBowled >= 12) {
            if (economy >= 5 && economy <6){
                economyBonus = economyBonus + 2;
            }
            else if (economy >= 4 && economy <5){
                economyBonus = economyBonus + 3;
            }
            else if (economy <4){
                economyBonus = economyBonus + 5;
            }
            else if (economy >=9 && economy <10){
                economyBonus = economyBonus - 2;
            }
            else if (economy >=10 && economy <11){
                economyBonus = economyBonus - 3;
            }
            else if (economy >=11 && economy <13){
                economyBonus = economyBonus - 5;
            }
            else if (economy >=13 && economy <15){
                economyBonus = economyBonus - 8;
            }
            else if (economy >=15){
                economyBonus = economyBonus - 10;
            }
        }


        Integer catchesPoints = catches * 4;
        Integer stumpingPoints = stumpings * 6;
        Integer directHitPoints = directHits * 10;

        battingPoints = runsPoints + foursPoints + sixesPoints + runsBonus+ strikeRateBonus;
        bowlingPoints = wicketPoints + economyBonus + hatrickBonus + maidenOverBonus + lbwOrBldPoints;
        fieldingPoints = catchesPoints + stumpingPoints + directHitPoints;
        Float totalGamePoints = battingPoints + bowlingPoints + fieldingPoints;

        dailyPlayerPoints.setRunsScored(runsScored);
        dailyPlayerPoints.setBallsFaced(balls);
        dailyPlayerPoints.setRunsPoints(runsPoints);
        dailyPlayerPoints.setFoursPoints(foursPoints);
        dailyPlayerPoints.setSixesPoints(sixesPoints);
        dailyPlayerPoints.setIsNotout(isNO);
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

        return dailyPlayerPoints;

    }



}
