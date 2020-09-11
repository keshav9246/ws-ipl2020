package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.websocket.server.ServerEndpoint;

@Getter
@Setter
public class PredictionRequest {

    private Integer gameNum;
    private String userId;
    private String team1;
    private String predictedTeam;
}
