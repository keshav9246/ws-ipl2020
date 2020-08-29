package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter
@Entity
@Component
public class User {

    private Integer playId;
    @Id
    private String userId;
    private String pwd;
    private Float predictionScore;
    private String role;
}
