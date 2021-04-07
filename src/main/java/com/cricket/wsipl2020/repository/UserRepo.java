package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.dto.MyCurrentRankResponse;
import com.cricket.wsipl2020.model.Player;
import com.cricket.wsipl2020.model.PointsTableResponse;
import com.cricket.wsipl2020.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, String> {

//    @Query(value = "Select * from user where user_id = :userName", nativeQuery = true)
//     User getUser(@Param("userName") String userName);

    @Modifying @Transactional
    @Query(value = "Update user set prediction_score = prediction_score + :pointsEarned where user_id in (:userIds)", nativeQuery = true)
    Integer updateUserPredictionPoints(@Param("userIds") List<String> userIds, @Param("pointsEarned") Double pointsEarned);

    @Modifying @Transactional
    @Query(value = "Update user set dream18score = dream18score + :dailyPlayerPoints where user_id in (:userIds)",nativeQuery = true)
    void updateDailyPoints(@Param("dailyPlayerPoints") Double dailyPlayerPoints, @Param("userIds")List<String> userIds);

    @Query(value = "Select u from User u where u.userId = :userId")
    List<User> fetchCompleteUserDetails(@Param("userId") String userId);

    @Query(value = "Select new com.cricket.wsipl2020.model.PointsTableResponse (u.userName, u.predictionScore) from User u order by u.predictionScore desc ")
    List<PointsTableResponse> fetchPredictionPointsTable();

//    @Query(value = "Select new com.cricket.wsipl2020.dto.MyCurrentRankResponse (u.userName, u.predictionScore, (Select count u from user where u.predictionScore )) from User u where u.userId = :userId ")
//    List<MyCurrentRankResponse> fetchMyPredictionPoints(String userId);

    @Query(value = "Select new com.cricket.wsipl2020.model.PointsTableResponse (u.userName, u.dream18Score) from User u order by u.dream18Score desc ")
    List<PointsTableResponse> fetchFantasyPointsTable();

    @Query(value = "Select u.user_id from user u where u.power_player = :playerName",nativeQuery = true)
    List<String> fetchUserIdsPP(@Param("playerName") String playerName);


}
