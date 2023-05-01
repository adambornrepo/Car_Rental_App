package com.g4.repository;

import com.g4.domain.PersonalRental;
import com.g4.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PersonalRentalRepository extends JpaRepository<PersonalRental, Long> {
    List<PersonalRental> findAllByReturnDate(LocalDate returnDate);

    @Query("SELECT pr FROM PersonalRental pr WHERE pr.status=:pReserved OR pr.status=:pRented")
    List<PersonalRental> findAllOngoingRentals(@Param("pReserved") RentalStatus reserved, @Param("pRented") RentalStatus rented);
}
