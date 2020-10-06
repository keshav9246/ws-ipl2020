package com.cricket.wsipl2020.controller;
import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.*;
import com.cricket.wsipl2020.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/iplt20")
public class UserController {

    @Autowired
    UserService userService;

//    @PostMapping("/auth")
//    public boolean authenticateUser(String userName, String pwd)
//    {
//        return userService.authenticate(userName, pwd);
//    }


    @GetMapping("/schedule")
    public List<Schedule> fetchSchedule()
    {
        return userService.fetchSchedule();
    }

    @GetMapping("/gamesToday")
    public List<Schedule> fetchGamesToday() {
        return userService.fetchTodaysGame();
    }

    @PostMapping("/submitPrediction")
    public void submitPrediciton(@RequestBody PredictionRequest request)
    {
        userService.submitPrediction(request.getGameNum(), request.getUserId(), request.getPredictedTeam());
    }
    @PostMapping("/submitWinningTeam")
    public void submitWinningTeam(@RequestBody WinnerRequest request)
    {
        userService.submitWinningTeam(request.getGameNum(), request.getWinningTeam(), request.getPointsEarned());
    }

    @GetMapping("/getPredictions")
    public List<PredictionPointsDTO> fetchPredictions()
    {
        return  userService.fetchPredictions();
    }

    @GetMapping("/myPredictions")
    public List<PredictionPointsDTO> fetchPredictionsByUser(String userId)
    {
        return  userService.fetchPredictionsByUser(userId);
    }

    @PostMapping("/submitScore")
    public void submitScore(@RequestBody Score playerScore){
        userService.submitScore(playerScore);
    }

    @GetMapping("/allUsers")
    public List<User> fetchAllUsers()
    {
        return userService.fetchAllUsers();
    }

    @GetMapping("/playerScores")
    public List<Score> fetchDailyPlayerScores(){
        return userService.fetchDailyScores();
    }

    @GetMapping("/playerPoints")
    public List<DailyPlayerPoints> fetchDailyPlayerFantasyPoints(){
        return userService.fetchDailyFantasyPoints();
    }

    @GetMapping("/userDetails")
    public List<User> fetchMyteam(String userId){
        return userService.fetchCompleteUserDetails(userId);
    }

    @GetMapping("/fetchPlayers")
    public List<Player> fetchPlayers(){
        return userService.fetchPlayers();
    }

    @GetMapping("/predictionPointsTable")
    public List<PointsTableResponse> fetchPredictionPointsTable(){
        return userService.fetchPredictionPointsTable();
    }

//    @GetMapping("/myPredictionPoints")
//    public PointsTableResponse fetchMyPredictionPoints(){
//        return userService.fetchPredictionPointsTable();
//    }

    @GetMapping("/fantasyPointsTable")
    public List<PointsTableResponse> fetchFantasyPointsTable(){
        return userService.fetchFantasyPointsTable();
    }

    @GetMapping("/allocations")
    public List<Allocations> getAllocations(){ return  userService.fetchAllocations();}

//    @GetMapping("/myPredictionPoints")
//    public PointsTableResponse fetcchMyFantasyPoints(){
//        return userService.fetchPredictionPointsTable();
//    }


}
