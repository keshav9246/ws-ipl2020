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
            if((LocalTime.now().plusMinutes(90).isBefore(game.getGameTime())))
            {
                predictionEligibleGames.add(game);
            }
        }
        return predictionEligibleGames;


    }

    public void submitPrediction(Integer gameId, String userId, String team1, String predictedTeam) {
        predictionRepo.submitPrediction(gameId,userId,predictedTeam);
        if(team1.equals(predictedTeam)) {
            scheduleRepo.submitTeam1Vote(gameId);
        }
        else {
            scheduleRepo.submitTeam2Vote(gameId);
        }

    }

    public void submitWinningTeam(Integer gameNum, String winningTeam){
        scheduleRepo.updateWinningTeam(gameNum, winningTeam);
    }

    public List<PredictionPointsDTO> fetchPredictions(String userId) {
        return predictionRepo.fetchPredictionList(userId);
    }



}
