package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WinnerRequest {

    private Integer gameNum;
    private String winningTeam;
    private Double pointsEarned;
}
