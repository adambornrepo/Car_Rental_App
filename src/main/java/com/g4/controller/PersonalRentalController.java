package com.g4.controller;

import com.g4.dto.PersonalRentalDTO;
import com.g4.service.PersonalRentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/per-rent")
public class PersonalRentalController {

    private PersonalRentalService personalRentalService;

    @Autowired
    public PersonalRentalController(PersonalRentalService personalRentalService) {
        this.personalRentalService = personalRentalService;
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<PersonalRentalDTO>> getAllOngoingRentals() {

        List<PersonalRentalDTO> rentals = personalRentalService.getAllOngoingRentals();

        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/find")
    public ResponseEntity<PersonalRentalDTO> getRentalById(@RequestParam("id") Long id) {

        PersonalRentalDTO found = personalRentalService.findPersonalRentalById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PersonalRentalDTO>> getRentalByPerCustomerPhoneNum(@RequestParam("phoneNum") String phoneNum) {

        List<PersonalRentalDTO> foundRentals = personalRentalService.getPersonalRentalByPerCustomerPhoneNum(phoneNum);
        return ResponseEntity.ok(foundRentals);
    }

    @GetMapping("/return")
    public ResponseEntity<List<PersonalRentalDTO>> getPersonalRentalByReturnDate(@RequestParam("date") LocalDate returnDate) {

        List<PersonalRentalDTO> foundRentals = personalRentalService.getPersonalRentalByReturnDate(returnDate);
        return ResponseEntity.ok(foundRentals);
    }

    @PostMapping
    public ResponseEntity<?> createRental(@Valid @RequestBody PersonalRentalDTO personalRentalDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        PersonalRentalDTO rentalDTO = personalRentalService.createPersonalRental(personalRentalDTO);

        if (rentalDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create rental");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rentalDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonalRental(@PathVariable Long id, @RequestBody PersonalRentalDTO rentalDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        PersonalRentalDTO personalRentalDTO = personalRentalService.updatePersonalRental(id, rentalDTO);

        if (personalRentalDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update rental");
        }

        return ResponseEntity.ok(personalRentalDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelRental(@PathVariable Long id) {

        personalRentalService.cancelPersonalRental(id);

        return ResponseEntity.ok("Personal rental successfully canceled");
    }


}
