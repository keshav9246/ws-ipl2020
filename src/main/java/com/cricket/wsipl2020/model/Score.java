package com.cricket.wsipl2020.model;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Score {

    @EmbeddedId
    private ScorePK scorePK;
    private Integer runsScored;
    private Integer ballsFaced;
    private Integer foursHit;
    private Integer sixesHit;
    private Boolean isNotout;

    private Float overs;
    private Integer runsConceded;
    private Integer wicketsTaken;
    private Integer bwldLbwCnb;
    private Integer maidenOvers;

    private Integer catchesTaken;
    private Integer directHits;

}
