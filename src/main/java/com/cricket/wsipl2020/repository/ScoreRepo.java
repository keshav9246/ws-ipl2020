package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.Score;
import com.cricket.wsipl2020.model.ScorePK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ScoreRepo extends CrudRepository<Score, ScorePK> {

    @Modifying @Transactional
    @Query(value = "Insert into score values(:gameNum,:playerName, :runsScored, :balls, :fours, :sixes, :isNO, :ballsBowled, :runsGiven, :bwldLb,:wickets,:bwldLb, :maidens,:hatrick, :catches, :directHits, :stumpings)", nativeQuery = true)
    public void submitScore(@Param("gameNum") Integer gameNum, @Param("playerName") String playerName, @Param("runsScored") Integer runsScored, @Param("balls") Integer balls, @Param("fours") Integer fours, @Param("sixes") Integer sixes, @Param("isNO") Boolean isNO,
                            @Param("ballsBowled") Integer ballsBowled, @Param("runsGiven") Integer runsGiven, @Param("wickets") Integer wickets, @Param("bwldLb") Integer bwldLb, @Param("maidens") Integer maidens, @Param("hatrick") Integer hatrick,
                            @Param("catches") Integer catches, @Param("directHits") Integer directHits, @Param("stumpings") Integer stumpings);

    @Query(value = "Select s from Score s order by s.scorePK.gameNum DESC")
    List<Score> fetchScores();


//    private Integer runsScored;
//    private Integer ballsFaced;
//    private Integer foursHit;
//    private Integer sixesHit;
//    private Boolean isNotout;
//
//    //Bowling points
//    private Float overs;
//    private Integer runsConceded;
//    private Integer wicketsTaken;
//    private Integer bwldLbwCnb;
//    private Integer maidenOvers;
//
//    //fielding points
//    private Integer catchesTaken;
//    private Integer directHits;
//    private Integer stumpings;

}
