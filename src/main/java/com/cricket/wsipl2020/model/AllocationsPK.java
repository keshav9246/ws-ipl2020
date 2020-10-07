package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class AllocationsPK implements Serializable {

    private Integer gameNum;
    private String userId;
    private String playerName;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AllocationsPK)) return false;
        AllocationsPK that = (AllocationsPK) o;
        return getGameNum() == that.getGameNum()
                && getUserId() == (that.getUserId())
                && getPlayerName() == (that.getPlayerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameNum(),getUserId(),getPlayerName());
    }
}
