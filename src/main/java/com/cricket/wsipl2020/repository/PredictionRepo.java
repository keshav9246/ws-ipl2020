package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.Prediction;
import com.cricket.wsipl2020.model.PredictionPK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PredictionRepo extends CrudRepository<Prediction, PredictionPK> {

    @Modifying @Transactional
      @Query( value = "Insert into prediction (game_num, user_id, prediction, points_gained) values (:gameNum, :userId, :predictedTeam, 0.0)", nativeQuery = true)
    void submitPrediction(@Param("gameNum") Integer gameNum, @Param("userId")String userId,@Param("predictedTeam") String predictedTeam);

    @Modifying @Transactional
    @Query( value = "Update prediction set prediction = :predictedTeam where game_num = :gameNum  and user_id = :userId", nativeQuery = true)
    void updatePredition(@Param("gameNum") Integer gameNum, @Param("userId")String userId,@Param("predictedTeam") String predictedTeam);

    @Modifying @Transactional
    @Query( value = "Update prediction set points_gained = :pointsEarned where game_num = :gameNum  and user_id in (:userIds)", nativeQuery = true)
    void updatePoints(@Param("gameNum") Integer gameNum, @Param("userIds")List<String> userIds,@Param("pointsEarned") Float pointsEarned);


    @Query( value = "Select count(*) from prediction where user_id = :userId and game_num = :gameNum", nativeQuery = true)
    Integer checkPrediction(@Param("gameNum") Integer gameNum, @Param("userId")String userId);


    @Query(value = "Select new com.cricket.wsipl2020.dto.PredictionPointsDTO (p.predictionPK.gameNum,p.predictionPK.userId, s.team1, s.team2, s.winningTeam, p.prediction, s.maxPoints, p.pointsGained)  from Prediction p\n" +
            "inner JOIN Schedule s on s.gameNum = p.predictionPK.gameNum\n" +
            "inner Join User u on u.userId = p.predictionPK.userId")
    List<PredictionPointsDTO> fetchPredictionList();

    @Query(value = "Select new com.cricket.wsipl2020.dto.PredictionPointsDTO (p.predictionPK.gameNum,p.predictionPK.userId, s.team1, s.team2, s.winningTeam, p.prediction, s.maxPoints, p.pointsGained)  from Prediction p\n" +
            "inner JOIN Schedule s on s.gameNum = p.predictionPK.gameNum\n" +
            "inner Join User u on u.userId = p.predictionPK.userId where p.predictionPK.userId = :userId")
    List<PredictionPointsDTO> fetchPredictionsByUser(@Param("userId") String userId);

    @Query( value = "Select user_id from prediction where game_num = :gameNum and prediction = :winningTeam", nativeQuery = true)
    List<String> fetchWinningUsers(@Param("gameNum") Integer gameNum, @Param("winningTeam") String winningTeam);

}

