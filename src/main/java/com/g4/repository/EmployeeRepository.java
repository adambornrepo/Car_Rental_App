package com.g4.repository;

import com.g4.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Optional<Employee> findByUsername(String username);
}
