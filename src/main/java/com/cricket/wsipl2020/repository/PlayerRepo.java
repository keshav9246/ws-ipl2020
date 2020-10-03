package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.Player;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PlayerRepo extends CrudRepository<Player, String> {

    @Modifying @Transactional
    @Query(value = "Update player set score = score + :dailyPlayerPoints , string_of_scores = CONCAT(string_of_scores, :score) where player_name = :playerName", nativeQuery = true)
     void updatePlayerScore(@Param("playerName") String playerName, @Param("dailyPlayerPoints") Float dailyPlayerPoints,  @Param("score") String score);

    @Query(value = "Select player_role from player where player_name = :playerName", nativeQuery = true)
     String getPlayerRole (@Param("playerName") String playerName);

    @Query(value = "Select is_captain from player where player_name = :playerName", nativeQuery = true)
    Boolean checkIfCaptain (@Param("playerName") String playerName);

    @Query(value = "Select * from player where score != 0 ORDER BY score DESC", nativeQuery = true)
    List<Player> fetchPlayers();

//    @Query(value = "Select p.powerPlayerFor from Player p where p.player_name = :playerName")
//    List<String> fetchPowerPlayer(@Param("playerName") String playerName);


}
