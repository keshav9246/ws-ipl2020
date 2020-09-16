package com.cricket.wsipl2020.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.util.List;

public class TeamPredictions {


    List<Player> mainTeam;
    List<Player> backupTeam;
    List<String> qualifyingTeams;
    Player orangeCap;
    Player purpleCap;

}
