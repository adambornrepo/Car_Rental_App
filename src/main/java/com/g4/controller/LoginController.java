package com.g4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class LoginController {

    @GetMapping("/employee")
    public ResponseEntity<?> employeeLogin(){

        return ResponseEntity.ok("dsf");
    }

    @GetMapping("/customer")
    public ResponseEntity<?> customerLogin(){

        return ResponseEntity.ok("dsf");
    }

}
