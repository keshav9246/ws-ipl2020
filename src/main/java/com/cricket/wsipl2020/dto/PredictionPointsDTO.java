package com.cricket.wsipl2020.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PredictionPointsDTO {
    private Integer gameNum;
    private String userId;
    private String team1;
    private String team2;
    private String winningTeam;
    private String prediction;
    private Float maxPoints;
    private Float pointsGained;
}
