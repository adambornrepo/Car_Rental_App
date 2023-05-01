package com.g4.repository;

import com.g4.domain.CorporateCustomer;
import com.g4.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Long> {

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Optional<CorporateCustomer> findByPhoneNumber(String phoneNumber);

    Optional<CorporateCustomer> findByUsername(String username);

    Optional<CorporateCustomer> findByEmail(String email);

    @Query("SELECT cc FROM CorporateCustomer cc WHERE cc.status=:pAvailable OR cc.status=:pHasRented")
    List<CorporateCustomer> findAllActive(@Param("pAvailable") CustomerStatus available, @Param("pHasRented") CustomerStatus hasRented);
}
