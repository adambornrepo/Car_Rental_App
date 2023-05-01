package com.g4.controller;

import com.g4.service.FleetRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corp-rent")
public class FleetRentalController {

    private FleetRentalService fleetRentalService;

    @Autowired
    public FleetRentalController(FleetRentalService fleetRentalService) {
        this.fleetRentalService = fleetRentalService;
    }


}
