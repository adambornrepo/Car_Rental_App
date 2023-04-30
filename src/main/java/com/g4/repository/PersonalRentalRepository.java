package com.g4.repository;

import com.g4.domain.PersonalRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PersonalRentalRepository extends JpaRepository<PersonalRental, Long> {
    List<PersonalRental> findAllByReturnDate(LocalDate returnDate);

}
