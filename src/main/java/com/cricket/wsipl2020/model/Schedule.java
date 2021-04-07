package com.cricket.wsipl2020.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.time.LocalDate;



@Entity
@Getter @Setter
public class Schedule {

    @Id
    private Integer gameNum;
    private LocalDate gameDate;
    private Time gameTime;
    private String team1;
    private String team2;
    private String team1_votes;
    private String team2_votes;
    private String winningTeam;
    private Double maxPoints;

}
