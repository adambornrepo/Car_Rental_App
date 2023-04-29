package com.g4.controller;

import com.g4.domain.PersonalCustomer;
import com.g4.dto.PersonalCustomerDTO;
import com.g4.service.PersonalCustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/per")
public class PersonalCustomerController {

    private PersonalCustomerService personalCustomerService;

    @Autowired
    public PersonalCustomerController(PersonalCustomerService personalCustomerService) {
        this.personalCustomerService = personalCustomerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody PersonalCustomerDTO personalCustomerDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        PersonalCustomer personalCustomer = personalCustomerService.createPersonalCustomer(personalCustomerDTO);

        if (personalCustomer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create personal customer");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Personal customer created successfully");
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonalCustomerDTO> getPersonalCustomerById(@PathVariable("id") Long id) {

        PersonalCustomerDTO found = personalCustomerService.findPersonalCustomerById(id);
        return ResponseEntity.ok(found);

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonalCustomer(@PathVariable("id") Long id, @Valid @RequestBody PersonalCustomerDTO personalCustomerDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        personalCustomerService.updatePersonalCustomer(id, personalCustomerDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Personal customer successfully updated");
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonalCustomer(@PathVariable("id") Long id) {

        personalCustomerService.deletePersonalCustomer(id);

        return ResponseEntity.ok("Personal customer successfully deleted");
    }

}
