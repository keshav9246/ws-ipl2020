package com.cricket.wsipl2020.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BowlingPoints {
    private Integer runsConceded;
    private Integer ballsBowled;
    private Integer wicketsTaken;
    private Double economy;
    private Integer wicketPoints ;
    private Integer economyBonus ;
    private Integer hatrickBonus ;
    private Integer maidenOverBonus;
    private Integer lbwOrBldPoints;
    private Double bowlingPoints;
}
