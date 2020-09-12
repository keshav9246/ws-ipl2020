package com.cricket.wsipl2020.service;
import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.Schedule;
import com.cricket.wsipl2020.model.User;
import com.cricket.wsipl2020.repository.PredictionRepo;
import com.cricket.wsipl2020.repository.ScheduleRepo;
import com.cricket.wsipl2020.repository.UserRepo;
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

    public boolean authenticate(String userName, String pwd) {
        User user =  userRepo.getUser(userName);
        if (user.getPwd().equals(pwd)) {
            return true;
        }
        else {
            return false;
        }
    }

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



}
