package com.g4.repository;

import com.g4.domain.FleetRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetRentalRepository extends JpaRepository<FleetRental, Long> {
}
