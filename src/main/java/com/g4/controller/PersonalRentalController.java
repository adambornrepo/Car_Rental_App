package com.g4.controller;

import com.g4.dto.PerRentalCreateUpdateDTO;
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

    @GetMapping
    public ResponseEntity<List<PersonalRentalDTO>> getAll() { //Staff

        List<PersonalRentalDTO> rentals = personalRentalService.getAllRentals();

        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<PersonalRentalDTO>> getAllOngoingRentals() { //Staff

        List<PersonalRentalDTO> rentals = personalRentalService.getAllOngoingRentals();

        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/find")
    public ResponseEntity<PersonalRentalDTO> getRentalById(@RequestParam("id") Long id) {  //  Staff - PerCustomer

        PersonalRentalDTO found = personalRentalService.findPersonalRentalById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/findCurByPer") //  Staff - PerCustomer
    public ResponseEntity<List<PersonalRentalDTO>> getRentalByPerCustomerPhoneNum(@RequestParam("phoneNum") String phoneNum) {

        List<PersonalRentalDTO> foundRentals = personalRentalService.getPersonalRentalByPerCustomerPhoneNum(phoneNum);
        return ResponseEntity.ok(foundRentals);
    }

    @GetMapping("/return") //  Staff
    public ResponseEntity<List<PersonalRentalDTO>> getPersonalRentalByReturnDate(@RequestParam("date") LocalDate returnDate) {

        List<PersonalRentalDTO> foundRentals = personalRentalService.getPersonalRentalByReturnDate(returnDate);
        return ResponseEntity.ok(foundRentals);
    }

    @GetMapping("/findByPer") //  Staff - PerCustomer
    public ResponseEntity<List<PersonalRentalDTO>> getAllPersonalRentalByPerCustomerPhoneNum(@RequestParam("phoneNum") String phoneNum) {

        List<PersonalRentalDTO> foundRentals = personalRentalService.getAllPersonalRentalByPerCustomerPhoneNum(phoneNum);
        return ResponseEntity.ok(foundRentals);
    }


    @PostMapping // PerCustomer
    public ResponseEntity<?> createRental(@Valid @RequestBody PerRentalCreateUpdateDTO perRentalCreateUpdateDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        PersonalRentalDTO rentalDTO = personalRentalService.createPersonalRental(perRentalCreateUpdateDTO);

        if (rentalDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create rental");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rentalDTO);
    }

    @PutMapping("/{id}") //  PerCustomer
    public ResponseEntity<?> updatePersonalRental(@PathVariable Long id, @RequestBody PerRentalCreateUpdateDTO rentalDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        PersonalRentalDTO personalRentalDTO = personalRentalService.updatePersonalRental(id, rentalDTO);

        if (personalRentalDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update rental");
        }

        return ResponseEntity.ok(personalRentalDTO);
    }


    @DeleteMapping("/{id}") // PerCustomer
    public ResponseEntity<?> cancelRental(@PathVariable Long id) {

        personalRentalService.cancelPersonalRental(id);

        return ResponseEntity.ok("Personal rental successfully canceled");
    }

    @PutMapping("/comp/{id}") //  Staff
    public ResponseEntity<?> returnPersonalRental(@PathVariable Long id) {

        personalRentalService.returnRental(id);

        return ResponseEntity.ok("Personal rental successfully completed");
    }


}
