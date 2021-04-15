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
    @Query(value = "Insert into score (game_num, player_name, runs_scored, balls_faced,fours_hit ,sixes_hit,is_notout, \n" +
            "balls_bowled,runs_conceded,wickets_taken,dots, bwld_lbw_cnb,maiden_overs,hatricks,\n" +
            "catches_taken,direct_hits,far_direct_hits,stumpings,did_win,isMom)\n" +
            "values (:gameNum,:playerName, :runsScored, :balls, :fours, :sixes, :isNO, :ballsBowled, :runsGiven, :wickets, :dots, :bwldLb, :maidens,:hatrick, :catches, :directHits, :farDirectHits, :stumpings, :isMOM, :didWin)", nativeQuery = true)
    public void submitScore(@Param("gameNum") Integer gameNum,
                            @Param("playerName") String playerName,
                            @Param("runsScored") Integer runsScored,
                            @Param("balls") Integer balls,
                            @Param("fours") Integer fours,
                            @Param("sixes") Integer sixes,
                            @Param("isNO") Boolean isNO,
                            @Param("ballsBowled") Integer ballsBowled,
                            @Param("runsGiven") Integer runsGiven,
                            @Param("wickets") Integer wickets,
                            @Param("dots") Integer dots,
                            @Param("bwldLb") Integer bwldLb,
                            @Param("maidens") Integer maidens,
                            @Param("hatrick") Integer hatrick,
                            @Param("catches") Integer catches,
                            @Param("directHits") Integer directHits,
                            @Param("farDirectHits") Integer farDirectHits,
                            @Param("stumpings") Integer stumpings,
                            @Param("didWin")Boolean didWin,
                            @Param("isMOM")Boolean isMOM);


    @Query(value = "Select s from Score s order by s.scorePK.gameNum DESC")
    List<Score> fetchScores();


//    private ScorePK scorePK;
//
//    //Batting Score
//    private Integer runsScored;
//    private Integer ballsFaced;
//    private Integer foursHit;
//    private Integer sixesHit;
//    private Boolean isNotout;
//
//    //Bowling points
//    private Integer ballsBowled;
//    private Integer runsConceded;
//    private Integer wicketsTaken;
//    private Integer dots;
//    private Integer bwldLbwCnb;
//    private Integer maidenOvers;
//    private Integer hatricks;
//
//    //fielding points
//    private Integer catchesTaken;
//    private Integer directHits;
//    private Integer farDirectHits;
//    private Integer stumpings;
//    private Boolean isMOM;
//    private Boolean didWin;

}
