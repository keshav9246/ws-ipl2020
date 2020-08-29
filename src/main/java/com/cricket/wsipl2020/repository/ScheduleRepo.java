package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.Schedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepo extends CrudRepository<Schedule, Integer> {

    @Query(value = "Select * from schedule", nativeQuery = true)
    List<Schedule> getSchedule();

    @Query(value = "Select * from schedule where game_date = :todaysDate", nativeQuery = true)
    List<Schedule> getGameByDate(@Param("todaysDate") LocalDate todaysDate);

    @Modifying @Transactional
    @Query(value = "Update schedule set team1_votes = team1_votes+1 where game_num =:gameId",nativeQuery = true)
    void submitTeam1Vote(@Param("gameId") Integer gameId);

    @Modifying @Transactional
    @Query(value = "Update schedule set team2_votes = team2_votes+1 where game_num =:gameId",nativeQuery = true)
    void submitTeam2Vote(@Param("gameId") Integer gameId);

    @Modifying @Transactional
    @Query(value = "Update schedule set winning_team = :winningTeam where game_num =:gameId",nativeQuery = true)
    void updateWinningTeam(@Param("gameId") Integer gameId, @Param("winningTeam") String winningTeam);

}
