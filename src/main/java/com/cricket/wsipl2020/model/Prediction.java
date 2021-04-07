package com.cricket.wsipl2020.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Prediction {

    @EmbeddedId
    private PredictionPK predictionPK;
    private String prediction;
    private Double pointsGained;
}
