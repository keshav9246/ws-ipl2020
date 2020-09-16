package com.cricket.wsipl2020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DailyPlayerPointsPK implements Serializable {
    private Integer gameNum;
    private String playerName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyPlayerPointsPK)) return false;
        DailyPlayerPointsPK that = (DailyPlayerPointsPK) o;
        return Objects.equals(gameNum, that.gameNum) &&
                Objects.equals(playerName, that.playerName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameNum, playerName);
    }
}
