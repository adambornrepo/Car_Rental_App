package com.g4.controller;

import com.g4.service.PersonalRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/per-rent")
public class PersonalRentalController {

    private PersonalRentalService personalRentalService;

    @Autowired
    public PersonalRentalController(PersonalRentalService personalRentalService) {
        this.personalRentalService = personalRentalService;
    }


}
