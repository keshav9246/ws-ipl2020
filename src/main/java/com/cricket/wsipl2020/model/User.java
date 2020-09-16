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

    private Integer playId;
    @Id
    private String userId;
    private Float predictionScore;
    private String userRole;
    @ManyToMany
    private List<Player> mainTeam;
    @ManyToMany
    private List<Player> backupTeam;
    private String qualifyingTeams;
    private String orangeCap;
    private String purpleCap;
    private Float dream18Score;



}
