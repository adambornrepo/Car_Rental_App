package com.g4.dto;

import com.g4.domain.Car;
import com.g4.enums.CarStatus;
import com.g4.enums.CarType;
import com.g4.enums.Department;
import com.g4.enums.FuelType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    @NotBlank(message = "Plate number cannot be empty")
    private String plateNumber;

    @NotBlank(message = "Brand name cannot be empty")
    private String brand;

    @NotBlank(message = "Model name cannot be empty")
    private String model;

    @NotNull(message = "Year cannot be null")
    private Integer productYear;

    @NotBlank(message = "Color cannot be null")
    private String color;

    @Positive(message = "Daily price cannot be negative")
    @NotNull(message = "Daily price cannot be null")
    private Double dailyPrice;

    @NotNull(message = "Car type should be selected")
    private CarType carType;

    @Positive(message = "Seat count must be positive")
    @NotNull(message = "Seat count cannot be null")
    private Integer seatCount;

    @NotNull(message = "Fuel type should be selected")
    private FuelType fuelType;

    @Positive(message = "Mileage cannot be negative")
    @NotNull(message = "Mileage cannot be null")
    private Integer mileage;

    @NotNull(message = "Status should be selected")
    private CarStatus status;

    @NotNull(message = "Department should be selected")
    private Department department;

    public CarDTO(Car car) {
        this.plateNumber = car.getPlateNumber();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.productYear = car.getProductYear();
        this.color = car.getColor();
        this.dailyPrice = car.getDailyPrice();
        this.carType = car.getCarType();
        this.seatCount = car.getSeatCount();
        this.fuelType = car.getFuelType();
        this.mileage = car.getMileage();
        this.status = car.getStatus();
        this.department = car.getDepartment();
    }

}
