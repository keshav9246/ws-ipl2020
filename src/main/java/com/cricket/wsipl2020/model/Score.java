package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Getter @Setter
public class Score {
    @EmbeddedId
    @Id
    private ScorePK scorePK;

    //Batting Score
    private Integer runsScored;
    private Integer ballsFaced;
    private Integer foursHit;
    private Integer sixesHit;
    private Boolean isNotout;

    //Bowling points
    private Integer ballsBowled;
    private Integer runsConceded;
    private Integer wicketsTaken;
    private Integer dots;
    private Integer bwldLbwCnb;
    private Integer maidenOvers;
    private Integer hatricks;

    //fielding points
    private Integer catchesTaken;
    private Integer directHits;
    private Integer farDirectHits;
    private Integer stumpings;
    private Boolean isMOM;
    private Boolean didWin;

    @Transient
    private List<String> mainUserIds;
    @Transient
    private List<String> backUpUserIds;
    @Transient
    private List<String> rivalBackUpUserIds;
}
