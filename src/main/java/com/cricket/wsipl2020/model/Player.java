package com.cricket.wsipl2020.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Player {

    @Id
    private String player_name;
    private String team;
    private Float score;
    private String playerRole;
    private boolean isCaptain;
    @ManyToMany
    private List<User> OwnedBy;
    @OneToMany
    private List<User> powerPlayerFor;

}
