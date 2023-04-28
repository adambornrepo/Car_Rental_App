package com.g4.service;

import com.g4.domain.Car;
import com.g4.exception.PlateNumberAlreadyExistException;
import com.g4.exception.ResourceNotFoundException;
import com.g4.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public void createCar(Car car) {
        if (carRepository.existsByPlateNumber(car.getPlateNumber())) {
            throw new PlateNumberAlreadyExistException("This plate number is already exist");
        }
        carRepository.save(car);
    }


    public Car findCar(Long id) {
        return carRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Car not found with this id : " + id));
    }

    public Car getByPlateNumber(String plateNumber) {
        return carRepository.
                findByPlateNumber(plateNumber).
                orElseThrow(() -> new RuntimeException("Car cannot found with this plate number : " + plateNumber));
    }

    public void deleteCar(Long id) {
        carRepository.delete(findCar(id));
    }


}
