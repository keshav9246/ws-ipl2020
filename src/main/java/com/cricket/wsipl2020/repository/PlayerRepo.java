package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.Player;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PlayerRepo extends CrudRepository<Player, String> {

    @Modifying @Transactional
    @Query(value = "Update player set score = score + :dailyPlayerPoints where player_name = :playerName", nativeQuery = true)
     void updatePlayerScore(@Param("playerName") String playerName, @Param("dailyPlayerPoints") Float dailyPlayerPoints);

    @Query(value = "Select player_role from player where player_name = :playerName", nativeQuery = true)
     String getPlayerRole (@Param("playerName") String playerName);
}
