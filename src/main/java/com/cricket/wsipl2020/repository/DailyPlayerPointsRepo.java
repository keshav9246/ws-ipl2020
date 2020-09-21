package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.DailyPlayerPoints;
import com.cricket.wsipl2020.model.DailyPlayerPointsPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DailyPlayerPointsRepo extends CrudRepository<DailyPlayerPoints, DailyPlayerPointsPK> {

    //aveDailyPlayerPoints(@Param(""))


}
