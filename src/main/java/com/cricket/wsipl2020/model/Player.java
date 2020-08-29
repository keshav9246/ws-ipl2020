package com.cricket.wsipl2020.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Player {
    @Id
    private String name;
    private String team;
    private Float score;
    private String role;
    private boolean isCaptain;

}
