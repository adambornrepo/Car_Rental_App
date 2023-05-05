package com.g4.service;

import com.g4.dto.FleetRentalDTO;
import com.g4.dto.PersonalRentalDTO;
import com.g4.repository.FleetRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class FleetRentalService {

    private CarService carService;
    private CorporateCustomerService corporateCustomerService;
    private FleetRentalRepository fleetRentalRepository;

    @Autowired
    public FleetRentalService(CarService carService, CorporateCustomerService corporateCustomerService, FleetRentalRepository fleetRentalRepository) {
        this.carService = carService;
        this.corporateCustomerService = corporateCustomerService;
        this.fleetRentalRepository = fleetRentalRepository;
    }

    Logger logger = Logger.getLogger(FleetRentalService.class.getName());

    public List<FleetRentalDTO> getAllRentals() {
        return null;
    }

    public FleetRentalDTO findFleetRentalById(Long id) {
        return null;
    }

    public List<FleetRentalDTO> getCorpRentalByPhoneNum(String phoneNum) {
        return null;
    }

    public FleetRentalDTO createFleetRental(FleetRentalDTO fleetRentalDTO) {
        return null;
    }
}
