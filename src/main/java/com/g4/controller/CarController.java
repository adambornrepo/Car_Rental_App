package com.g4.controller;

import com.g4.dto.CarDTO;
import com.g4.enums.Department;
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
    public ResponseEntity<List<CarDTO>> getAll() {//Staff
        List<CarDTO> cars = carService.getAll();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/ava")
    public ResponseEntity<List<CarDTO>> getAllAvailables() {//Staff
        List<CarDTO> cars = carService.getAllAvailables();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/find")
    public ResponseEntity<CarDTO> getCarByPlateNumber(@RequestParam("plate") String plateNumber) {//Staff
        CarDTO found = carService.findCarByPlateNumber(plateNumber);

        return ResponseEntity.ok(found);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CarDTO>> getCarByDepartment(@RequestParam("dept") Department department) {//Staff
        List<CarDTO> carDTOList = carService.findCarByDepartment(department);

        return ResponseEntity.ok(carDTOList);
    }

    @GetMapping("/findAva")
    public ResponseEntity<List<CarDTO>> getAllAvaCarByDepartment(@RequestParam("dept") Department department) {//Staff - Customer(s)
        List<CarDTO> carDTOList = carService.findCarAvaDept(department);

        return ResponseEntity.ok(carDTOList);
    }

    @PostMapping
    public ResponseEntity<?> addCar(@Valid @RequestBody CarDTO carDTO, BindingResult result) {//Staff

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        carService.createCar(carDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Car is created");
        response.put("status", "true");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{plate}")  //Staff
    public ResponseEntity<?> updateCar(@PathVariable("plate") String plateNumber, @Valid @RequestBody CarDTO carDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        carService.updateCar(plateNumber, carDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Car successfully updated");
    }

    @DeleteMapping("/{plate}")
    public ResponseEntity<Map<String, String>> deleteCar(@PathVariable("plate") String plateNumber) { //Staff

        carService.deleteCar(plateNumber);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Car is deleted");
        response.put("status", "true");

        return ResponseEntity.ok(response);
    }


}
