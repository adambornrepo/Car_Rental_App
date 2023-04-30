package com.g4.repository;

import com.g4.domain.Car;
import com.g4.domain.PersonalCustomer;
import com.g4.domain.PersonalRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalRentalRepository extends JpaRepository<PersonalRental, Long> {

//    Optional<PersonalRental> findPersonalRentalByPersonalCustomer(PersonalCustomer personal);
//
//    Optional<PersonalRental> findPersonalRentalByCar(Car car);
}
