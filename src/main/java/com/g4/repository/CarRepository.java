package com.g4.repository;

import com.g4.domain.Car;
import com.g4.enums.CarStatus;
import com.g4.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {


    Optional<Car> findByPlateNumber(String plateNumber);

    boolean existsByPlateNumber(String plateNumber);

    @Query("SELECT c FROM Car c WHERE c.status=:pStatus")
    List<Car> findAllAvailables(@Param("pStatus") CarStatus carStatus);

    @Query("SELECT c FROM Car c WHERE c.department=:pDepartment")
    List<Car> findAllDepartment(@Param("pDepartment") Department department);
}
