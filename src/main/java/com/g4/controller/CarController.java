package com.g4.controller;

import com.g4.dto.CarDTO;
import com.g4.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<List<CarDTO>> getAll() {
        List<CarDTO> cars = carService.getAll();
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<?> addCar(@Valid @RequestBody CarDTO carDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        carService.createCar(carDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Car is created");
        response.put("status", "true");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<CarDTO> getCarById(@RequestParam("plate") String plateNumber) {
        CarDTO found = carService.findCarByPlateNumber(plateNumber);

        return ResponseEntity.ok(found);
    }

    @PutMapping("/{plate}")
    public ResponseEntity<?> updateCar(@PathVariable("plate") String plateNumber, @Valid @RequestBody CarDTO carDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        carService.updateCar(plateNumber, carDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Car successfully updated");
    }

    @DeleteMapping("/{plate}")
    public ResponseEntity<Map<String, String>> deleteCar(@PathVariable("plate") String plateNumber) {

        carService.deleteCar(plateNumber);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Car is deleted");
        response.put("status", "true");

        return ResponseEntity.ok(response);
    }


}
