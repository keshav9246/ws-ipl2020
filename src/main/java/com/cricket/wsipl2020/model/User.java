package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Component
public class User {

    @Id
    private String userId;
    private String userName;
    private Double predictionScore;
    @ManyToMany
    private List<Player> mainTeam;
    private String powerPlayer;
    @ManyToMany
    private List<Player> backupTeam;
    private String qualifyingTeams;
    private String orangeCap;
    private String purpleCap;
    private String winningTeam;
    private Double dream18Score;

}
