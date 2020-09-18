package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
public class Player {

    @Id
    private String player_name;
    private String team;
    private Float score;
    private String playerRole;
    private boolean isCaptain;
//    @ManyToMany
//    private List<User> OwnedBy;
//    @OneToMany (mappedBy = "player")
//    private List<User> powerPlayerFor;

}
