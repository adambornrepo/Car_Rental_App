package com.g4.controller;

import com.g4.domain.CorporateCustomer;
import com.g4.dto.CorporateCustomerDTO;
import com.g4.dto.CorporateCustomerLoginDTO;
import com.g4.dto.PersonalCustomerDTO;
import com.g4.dto.PersonalCustomerLoginDTO;
import com.g4.service.CorporateCustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corp")
public class CorporateCustomerController {
    private CorporateCustomerService corporateCustomerService;

    @Autowired
    public CorporateCustomerController(CorporateCustomerService corporateCustomerService) {
        this.corporateCustomerService = corporateCustomerService;
    }


    @GetMapping("/{phoneNum}")
    public ResponseEntity<CorporateCustomerDTO> getCorporateCustomerByPhoneNum(@PathVariable("phoneNum") String phoneNumber) {

        CorporateCustomerDTO found = corporateCustomerService.findCorporateCustomerByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(found);

    }


    @GetMapping
    public ResponseEntity<?> getAllCorporateCustomer() {

        List<CorporateCustomerDTO> corporateCustomerDTOList = corporateCustomerService.getAllCorporateCustomer();

        return ResponseEntity.ok(corporateCustomerDTOList);
    }

    @GetMapping("allActive")
    public ResponseEntity<?> getAllActiveCorporateCustomer() {

        List<CorporateCustomerDTO> corporateCustomerDTOList = corporateCustomerService.getAllActiveCorporateCustomer();

        return ResponseEntity.ok(corporateCustomerDTOList);
    }

    @GetMapping("/login")
    public ResponseEntity<?> getCorporateCustomerByUsername(@Valid @RequestBody CorporateCustomerLoginDTO loginDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        String fullName = corporateCustomerService.getCorporateCustomerByUsername(loginDTO);

        return ResponseEntity.ok(fullName);
    }



    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody CorporateCustomerDTO corporateCustomerDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        CorporateCustomer corporateCustomer = corporateCustomerService.createCorporateCustomer(corporateCustomerDTO);

        if (corporateCustomer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create corporate customer");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Corporate customer created successfully");
    }

    @PutMapping("/{phoneNum}")
    public ResponseEntity<?> updateCorporateCustomer(@PathVariable("phoneNum") String phoneNumber, @Valid @RequestBody CorporateCustomerDTO corporateCustomerDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        corporateCustomerService.updateCorporateCustomer(phoneNumber, corporateCustomerDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Corporate customer successfully updated");
    }


    @DeleteMapping("/{phoneNum}")
    public ResponseEntity<?> deleteCorporateCustomer(@PathVariable("phoneNum") String phoneNumber) {

        corporateCustomerService.deleteCorporateCustomer(phoneNumber);

        return ResponseEntity.ok("Corporate customer successfully deleted");
    }


}
