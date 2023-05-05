package com.g4.repository;

import com.g4.domain.FleetRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FleetRentalRepository extends JpaRepository<FleetRental, Long> {

}
