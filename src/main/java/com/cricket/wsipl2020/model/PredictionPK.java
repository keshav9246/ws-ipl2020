package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class PredictionPK implements Serializable{


    private Integer gameNum;
    private String userId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PredictionPK)) return false;
        PredictionPK that = (PredictionPK) o;
        return getGameNum() == that.getGameNum()
                && getUserId() == (that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameNum(),getUserId());
    }
}