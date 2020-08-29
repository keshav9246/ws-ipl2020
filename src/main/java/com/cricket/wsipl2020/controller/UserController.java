package com.cricket.wsipl2020.controller;

import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.Schedule;
import com.cricket.wsipl2020.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/iplt20")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/auth")
    public boolean authenticateUser(String userName, String pwd)
    {
        return userService.authenticate(userName, pwd);
    }


    @GetMapping("/schedule")
    public List<Schedule> fetchSchedule()
    {
        return userService.fetchSchedule();
    }

    @GetMapping("/gamesToday")
    public List<Schedule> fetchGamesToday()
    {
        return userService.fetchTodaysGame();

    }

    @PostMapping("/submitPrediciton")
    public void submitPrediciton(Integer gameNum, String userId, String team1, String predictedTeam )
    {
        userService.submitPrediction(gameNum, userId,team1, predictedTeam);
    }
    @PostMapping("/submitWinningTeam")
    public void submitWinningTeam(Integer gameNum, String winningTeam )
    {
        userService.submitWinningTeam(gameNum, winningTeam);
    }

    @GetMapping("/getPredictions")
    public List<PredictionPointsDTO> fetchPredictions(String userId)
    {
        return  userService.fetchPredictions(userId);
    }


}
