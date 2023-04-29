package com.g4.repository;

import com.g4.domain.PersonalCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCustomerRepository extends JpaRepository<PersonalCustomer, Long> {
    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}
