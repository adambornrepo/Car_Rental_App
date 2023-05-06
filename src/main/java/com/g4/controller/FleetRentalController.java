package com.g4.controller;

import com.g4.dto.FleetRentalCreateDTO;
import com.g4.dto.FleetRentalDTO;
import com.g4.service.FleetRentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corp-rent")
public class FleetRentalController {

    private FleetRentalService fleetRentalService;

    @Autowired
    public FleetRentalController(FleetRentalService fleetRentalService) {
        this.fleetRentalService = fleetRentalService;
    }

    @GetMapping
    public ResponseEntity<List<FleetRentalDTO>> getAll() { // Staff

        List<FleetRentalDTO> rentals = fleetRentalService.getAllRentals();

        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/find") // Staff
    public ResponseEntity<FleetRentalDTO> getRentalById(@RequestParam("id") Long id) {

        FleetRentalDTO found = fleetRentalService.findFleetRentalById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/findCurByCorp") // Staff - CorpCustomer
    public ResponseEntity<List<FleetRentalDTO>> getRentalByCorpCustomerPhoneNum(@RequestParam("phoneNum") String phoneNum) {

        List<FleetRentalDTO> foundRentals = fleetRentalService.getCorpRentalByPhoneNum(phoneNum);
        return ResponseEntity.ok(foundRentals);
    }

    @PostMapping // Staff
    public ResponseEntity<?> createRental(@Valid @RequestBody FleetRentalCreateDTO fleetRentalDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        FleetRentalDTO rentalDTO = fleetRentalService.createFleetRental(fleetRentalDTO);

        if (rentalDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create rental");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rentalDTO);
    }

    @PutMapping("/comp/{id}") //  Staff
    public ResponseEntity<?> returnFleetRental(@PathVariable Long id) {

        fleetRentalService.returnRental(id);

        return ResponseEntity.ok("Fleet rental successfully completed");
    }


}
