package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ScorePK implements Serializable {

    private Integer gameNum;
    private String playerName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScorePK)) return false;
        ScorePK that = (ScorePK) o;
        return getGameNum() == that.getGameNum()
                && getPlayerName() == (that.getPlayerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameNum(),getPlayerName());
    }
}
