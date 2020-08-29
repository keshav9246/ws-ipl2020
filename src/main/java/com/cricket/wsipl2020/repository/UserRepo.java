package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, String> {

    @Query(value = "Select * from user where user_id = :userName", nativeQuery = true)
     User getUser(@Param("userName") String userName);



}
