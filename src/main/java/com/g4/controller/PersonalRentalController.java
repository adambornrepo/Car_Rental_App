package com.g4.controller;

import com.g4.domain.PersonalRental;
import com.g4.dto.PersonalRentalDTO;
import com.g4.service.PersonalRentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<PersonalRentalDTO>> getAllRentals() {

        List<PersonalRentalDTO> rentals = personalRentalService.getAllRentals();

        return ResponseEntity.ok(rentals);
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
    /*

    bugun teslimi olan carları listele get + returnDate

    id ile rental bul tamam

    carDTO ile rental bul çalışmıyor

    personalCustomer ile Rental bul çalışmıyor

    RentalStatus a göre listele tamam sanırım

    update

    delete -->  burada değişiklik yapma işinini düşün rented den sonra update olmaması lazım

     */


    @GetMapping("/{id}")
    public ResponseEntity<PersonalRentalDTO> getRentalById(@PathVariable Long id) {

        PersonalRentalDTO found = personalRentalService.findPersonalRentalById(id);
        return ResponseEntity.ok(found);
    }

//    @GetMapping("/{plate}")
//    public ResponseEntity<PersonalRentalDTO> getRentalByCarPlateNum(@PathVariable String plateNumber) {
//
//        PersonalRentalDTO found = personalRentalService.findPersonalRentalByCarPlateNum(plateNumber);
//        return ResponseEntity.ok(found);
//    }
//
//    @GetMapping("/{perPhone}")
//    public ResponseEntity<PersonalRentalDTO> getRentalByPerCustomerPhoneNum(@PathVariable String phoneNumber) {
//
//        PersonalRentalDTO found = personalRentalService.findPersonalRentalByPerCustomerPhoneNum(phoneNumber);
//        return ResponseEntity.ok(found);
//    }



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
