package com.cricket.wsipl2020.repository;

import com.cricket.wsipl2020.model.Allocations;
import com.cricket.wsipl2020.model.AllocationsPK;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AllocationsRepo extends CrudRepository<Allocations,AllocationsPK> {

    @Transactional @Modifying
    @Query(value = "Insert into allocations values(:gameNum, :userName, :playerName, :score)", nativeQuery = true)
    void addAllocation(@Param("gameNum") Integer gameNum, @Param("userName") String userName,@Param("playerName") String playerName,@Param("score") Double score);


    @Query(value = "Select a from Allocations a order by a.allocationsPK.gameNum DESC, a.allocationsPK.playerName")
    List<Allocations> fetchAllocations();
}
