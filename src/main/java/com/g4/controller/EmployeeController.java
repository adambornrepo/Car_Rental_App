package com.g4.controller;

import com.g4.domain.Employee;
import com.g4.dto.EmployeeDTO;
import com.g4.dto.EmployeeLoginDTO;
import com.g4.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> signUp(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        Employee employee = employeeService.createEmployee(employeeDTO);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create employee");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    @GetMapping("/{phoneNum}")
    public ResponseEntity<EmployeeDTO> getEmployeeByPhoneNum(@PathVariable("phoneNum") String phoneNumber) {

        EmployeeDTO found = employeeService.getEmployeeByPhoneNum(phoneNumber);
        return ResponseEntity.ok(found);

    }

    @PutMapping("/{phoneNum}")
    public ResponseEntity<?> updateEmployee(@PathVariable("phoneNum") String phoneNumber, @Valid @RequestBody EmployeeDTO employeeDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        employeeService.updateEmployee(phoneNumber, employeeDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Employee successfully updated");
    }

    @DeleteMapping("/{phoneNum}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("phoneNum") String phoneNumber) {

        employeeService.deleteEmployee(phoneNumber);

        return ResponseEntity.ok("Employee successfully deleted");
    }

    @GetMapping("/login")
    public ResponseEntity<?> getEmployeeByUsername(@Valid @RequestBody EmployeeLoginDTO loginDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        String fullName = employeeService.getEmployeeByUsername(loginDTO);

        return ResponseEntity.ok(fullName);
    }


}
