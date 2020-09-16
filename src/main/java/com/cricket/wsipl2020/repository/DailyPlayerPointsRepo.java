package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.DailyPlayerPoints;
import com.cricket.wsipl2020.model.DailyPlayerPointsPK;
import org.springframework.data.repository.CrudRepository;

public interface DailyPlayerPointsRepo extends CrudRepository<DailyPlayerPoints, DailyPlayerPointsPK> {


}
