package com.cricket.wsipl2020.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter @Setter
public class Schedule {

    @Id
    private Integer gameNum;
    private LocalDate gameDate;
    private LocalTime gameTime;
    private String team1;
    private String team2;
    private String team1Votes;
    private String team2Votes;
    private String winningTeam;
    private Float maxPoints;

}
