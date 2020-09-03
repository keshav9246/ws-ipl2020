package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.dto.PredictionPointsDTO;
import com.cricket.wsipl2020.model.Prediction;
import com.cricket.wsipl2020.model.PredictionPK;
import org.hibernate.annotations.SQLInsert;
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


    @Query(value = "Select new com.cricket.wsipl2020.dto.PredictionPointsDTO (u.playId, p.predictionPK.gameNum,p.predictionPK.userId, s.team1, s.team2, s.winningTeam, p.prediction, s.maxPoints, p.pointsGained)  from Prediction p\n" +
            "inner JOIN Schedule s on s.gameNum = p.predictionPK.gameNum\n" +
            "inner Join User u on u.userId = p.predictionPK.userId\n" +
            "where u.playId = (Select u.playId from User u where u.userId = :userId)")
    List<PredictionPointsDTO> fetchPredictionList(@Param("userId") String userId);

}

