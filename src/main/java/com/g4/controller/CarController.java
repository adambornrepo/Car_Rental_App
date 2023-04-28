package com.g4.controller;

import com.g4.domain.Car;
import com.g4.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAll() {
        List<Car> cars = carService.getAll();
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addCar(@Valid @RequestBody Car car) {
        carService.createCar(car);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Car is created");
        response.put("status", "true");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<Car> getCar(@RequestParam("id") Long id) {
        Car car = carService.findCar(id);

        return ResponseEntity.ok(car);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCar(@PathVariable("id") Long id) {

        carService.deleteCar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Car is deleted");
        response.put("status", "true");

        return ResponseEntity.ok(response);
    }


}
