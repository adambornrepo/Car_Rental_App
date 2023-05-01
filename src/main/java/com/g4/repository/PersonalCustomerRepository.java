package com.g4.repository;

import com.g4.domain.PersonalCustomer;
import com.g4.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalCustomerRepository extends JpaRepository<PersonalCustomer, Long> {
    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<PersonalCustomer> findByPhoneNumber(String phoneNumber);

    Optional<PersonalCustomer> findByUsername(String username);

    @Query("SELECT pc FROM PersonalCustomer pc WHERE pc.status=:pAvailable OR pc.status=:pHasRented")
    List<PersonalCustomer> findAllActive(@Param("pAvailable") CustomerStatus available, @Param("pHasRented") CustomerStatus hasRented);
}
