package com.g4.service;

import com.g4.domain.Car;
import com.g4.dto.CarDTO;
import com.g4.enums.CarStatus;
import com.g4.enums.Department;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.UniqueValueAlreadyExistException;
import com.g4.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> getAll() {
        List<CarDTO> carDTOList = new ArrayList<>();

        carRepository.findAll().forEach(car -> carDTOList.add(new CarDTO(car)));

        return carDTOList;
    }

    public void createCar(CarDTO carDTO) {

        if (carRepository.existsByPlateNumber(carDTO.getPlateNumber())) {
            throw new UniqueValueAlreadyExistException("This plate number is already exist");
        }

        Car car = new Car();

        car.setPlateNumber(carDTO.getPlateNumber());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setProductYear(carDTO.getProductYear());
        car.setColor(carDTO.getColor());
        car.setDailyPrice(carDTO.getDailyPrice());
        car.setCarType(carDTO.getCarType());
        car.setSeatCount(carDTO.getSeatCount());
        car.setFuelType(carDTO.getFuelType());
        car.setMileage(carDTO.getMileage());
        car.setDepartment(carDTO.getDepartment());

        carRepository.save(car);
    }

//  Car controller use plateNum not ID anymore
//    public CarDTO findCarById(Long id) {
//        Car car = carRepository.
//                findById(id).
//                orElseThrow(() -> new ResourceNotFoundException("Car not found with this id : " + id));
//
//        return new CarDTO(car);
//    }

    public CarDTO findCarByPlateNumber(String plateNumber) {
        Car car = carRepository.
                findByPlateNumber(plateNumber).
                orElseThrow(() -> new RuntimeException("Car cannot found with this plate number : " + plateNumber));

        return new CarDTO(car);
    }

    public Car findCar(String plateNumber) {

        return carRepository.
                findByPlateNumber(plateNumber).
                orElseThrow(() -> new RuntimeException("Car cannot found with this plate number : " + plateNumber));
    }

    public void deleteCar(String plateNumber) {
        CarDTO found = findCarByPlateNumber(plateNumber);
        found.setStatus(CarStatus.PASSIVE);
        updateCar(plateNumber, found);
    }


    public void updateCar(String plateNumber, CarDTO carDTO) {

        Car car = carRepository.findByPlateNumber(plateNumber).
                orElseThrow(() -> new ResourceNotFoundException("Car not found with this plateNumber : " + plateNumber));

        if (!car.getPlateNumber().equals(carDTO.getPlateNumber())
                && carRepository.findByPlateNumber(carDTO.getPlateNumber()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Plate number already exists");
        }

        car.setPlateNumber(carDTO.getPlateNumber());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setProductYear(carDTO.getProductYear());
        car.setColor(carDTO.getColor());
        car.setDailyPrice(carDTO.getDailyPrice());
        car.setCarType(carDTO.getCarType());
        car.setSeatCount(carDTO.getSeatCount());
        car.setFuelType(carDTO.getFuelType());
        car.setMileage(carDTO.getMileage());
        car.setDepartment(carDTO.getDepartment());
        car.setStatus(carDTO.getStatus());

        carRepository.save(car);

    }

    public List<CarDTO> getAllAvailables() {

        List<CarDTO> carDTOList = new ArrayList<>();

        carRepository.findAllAvailables(CarStatus.AVAILABLE).forEach(car -> carDTOList.add(new CarDTO(car)));

        return carDTOList;
    }


    public List<CarDTO> findCarByDepartment(Department department) {

        List<CarDTO> carDTOList = new ArrayList<>();

        carRepository.findAllDepartment(department).forEach(car -> carDTOList.add(new CarDTO(car)));

        return carDTOList;
    }

    public List<CarDTO> findCarAvaDept(Department department) {

        return getAllAvailables()
                .stream()
                .filter(carDTO -> carDTO.getDepartment().equals(department))
                .toList();
    }
}
