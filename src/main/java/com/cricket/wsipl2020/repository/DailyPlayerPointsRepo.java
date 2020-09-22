package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.DailyPlayerPoints;
import com.cricket.wsipl2020.model.DailyPlayerPointsPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyPlayerPointsRepo extends CrudRepository<DailyPlayerPoints, DailyPlayerPointsPK> {

    @Query (value = "Select p from DailyPlayerPoints p order by p.dailyPlayerPointsPK.gameNum DESC")
    List<DailyPlayerPoints> fetchDailyPlayerPoints();


}
