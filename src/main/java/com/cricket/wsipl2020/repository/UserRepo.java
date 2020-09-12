package com.cricket.wsipl2020.repository;

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

    @Query(value = "Select * from user where user_id = :userName", nativeQuery = true)
     User getUser(@Param("userName") String userName);

    @Modifying @Transactional
    @Query(value = "Update user set prediction_score = prediction_score + :pointsEarned where user_id in (:userIds)", nativeQuery = true)
    Integer updateUserPredictionPoints(@Param("userIds") List<String> userIds, @Param("pointsEarned") Float pointsEarned);



}
