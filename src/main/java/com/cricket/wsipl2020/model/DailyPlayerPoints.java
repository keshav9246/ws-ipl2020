package com.cricket.wsipl2020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter@Setter
public class DailyPlayerPoints {

    @EmbeddedId
    private DailyPlayerPointsPK dailyPlayerPointsPK;
    private Integer runsScored;
    private Integer ballsFaced;
//    private Integer foursHit;
//    private Integer sixesHit;
    private Double strikeRate;
    private String wasNO;
    private Double runsPoints ;
    private Double foursPoints ;
    private Integer sixesPoints ;
    private Integer runsBonus;
    private Integer strikeRateBonus;
    private Double battingPoints;

    //Bowling points

    private Integer runsConceded;
    private Integer ballsBowled;
    private Integer wicketsTaken;
    private Double economy;
    private Double dotsPints;
    private Integer wicketPoints ;
    private Integer economyBonus ;
    private Integer hatrickBonus ;
    private Integer maidenOverBonus;
    private Integer lbwOrBldPoints;
    private Double bowlingPoints;


    //fielding points
    private Integer catchesPoints ;
    private Integer stumpingPoints ;
    private Integer directHitPoints;
    private Integer fieldingPoints;

    private Integer momOrWinPoints;
    private Double totalGamePoints;
    private String assignedTo;

}
