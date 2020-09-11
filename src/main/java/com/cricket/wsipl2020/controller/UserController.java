package com.cricket.wsipl2020.controller;
import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.PredictionRequest;
import com.cricket.wsipl2020.model.Schedule;
import com.cricket.wsipl2020.model.WinnerRequest;
import com.cricket.wsipl2020.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Schedule> fetchGamesToday() {
        return userService.fetchTodaysGame();
    }

    @PostMapping("/submitPrediciton")
    public void submitPrediciton(@RequestBody PredictionRequest request)
    {
        userService.submitPrediction(request.getGameNum(), request.getUserId(), request.getTeam1(), request.getPredictedTeam());
    }
    @PostMapping("/submitWinningTeam")
    public void submitWinningTeam(@RequestBody WinnerRequest request)
    {
        userService.submitWinningTeam(request.getGameNum(), request.getWinningTeam());
    }

    @GetMapping("/getPredictions")
    public List<PredictionPointsDTO> fetchPredictions(String userId)
    {
        return  userService.fetchPredictions(userId);
    }


}
