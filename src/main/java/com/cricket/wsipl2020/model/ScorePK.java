package com.cricket.wsipl2020.model;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Embeddable
public class ScorePK implements Serializable {

    private Integer gameNum;
    private String playerName;
}
