package com.g4.controller;

import com.g4.domain.PersonalCustomer;
import com.g4.dto.PersonalCustomerDTO;
import com.g4.dto.PersonalCustomerLoginDTO;
import com.g4.service.PersonalCustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/per")
public class PersonalCustomerController {

    private PersonalCustomerService personalCustomerService;

    @Autowired
    public PersonalCustomerController(PersonalCustomerService personalCustomerService) {
        this.personalCustomerService = personalCustomerService;
    }

    @PostMapping("/signup")  //PerCustomer
    public ResponseEntity<?> signUp(@Valid @RequestBody PersonalCustomerDTO personalCustomerDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input bu kısım çalıştı");
        }

        PersonalCustomer personalCustomer = personalCustomerService.createPersonalCustomer(personalCustomerDTO);

        //use it
        if (personalCustomer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create personal customer");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Personal customer created successfully");
    }


    @GetMapping("/{phoneNum}") //PerCustomer - Staff
    public ResponseEntity<PersonalCustomerDTO> getPersonalCustomerByPhoneNum(@PathVariable("phoneNum") String phoneNumber) {

        PersonalCustomerDTO found = personalCustomerService.findPersonalCustomerByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(found);

    }


    @PutMapping("/{phoneNum}") //PerCustomer - Staff
    public ResponseEntity<?> updatePersonalCustomer(@PathVariable("phoneNum") String phoneNumber, @Valid @RequestBody PersonalCustomerDTO personalCustomerDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        personalCustomerService.updatePersonalCustomer(phoneNumber, personalCustomerDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Personal customer successfully updated");
    }


    @DeleteMapping("/{phoneNum}") //PerCustomer - Staff
    public ResponseEntity<?> deletePersonalCustomer(@PathVariable("phoneNum") String phoneNumber) {

        personalCustomerService.deletePersonalCustomer(phoneNumber);

        return ResponseEntity.ok("Personal customer successfully deleted");
    }

    @GetMapping
    public ResponseEntity<?> getAllPersonalCustomer() { // Staff

        List<PersonalCustomerDTO> personalCustomerList = personalCustomerService.getAllPersonalCustomer();

        return ResponseEntity.ok(personalCustomerList);
    }

    @GetMapping("/allActive")  // Staff
    public ResponseEntity<?> getAllActivePersonalCustomer() {

        List<PersonalCustomerDTO> personalCustomerList = personalCustomerService.getAllActivePersonalCustomer();

        return ResponseEntity.ok(personalCustomerList);
    }

    @PostMapping("/login") //PerCustomer
    public ResponseEntity<?> getPersonalCustomerByUsername(@Valid @RequestBody PersonalCustomerLoginDTO loginDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        Map<String, String> response = personalCustomerService.getPersonalCustomerByUsername(loginDTO);


        return ResponseEntity.ok(response);
    }


}
