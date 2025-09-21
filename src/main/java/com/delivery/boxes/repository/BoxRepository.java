package com.delivery.boxes.repository;

import com.delivery.boxes.entity.Box;
import com.delivery.boxes.entity.BoxState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

    Optional<Box> findByTxref(String txref);

    @Query("SELECT b FROM Box b WHERE b.state IN ('IDLE', 'LOADED') AND b.batteryCapacity >= 25")
    List<Box> findAvailableBoxesForLoading();

    List<Box> findByState(BoxState state);

    boolean existsByTxref(String txref);
}