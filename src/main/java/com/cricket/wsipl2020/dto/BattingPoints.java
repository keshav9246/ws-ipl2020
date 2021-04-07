package com.cricket.wsipl2020.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattingPoints {

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
}
