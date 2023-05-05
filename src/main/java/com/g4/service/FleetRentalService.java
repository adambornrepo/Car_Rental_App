package com.g4.service;

import com.g4.domain.Car;
import com.g4.domain.CorporateCustomer;
import com.g4.domain.FleetRental;
import com.g4.domain.PersonalRental;
import com.g4.dto.CarDTO;
import com.g4.dto.CorporateCustomerDTO;
import com.g4.dto.FleetRentalDTO;
import com.g4.dto.PersonalRentalDTO;
import com.g4.enums.CarStatus;
import com.g4.enums.CustomerStatus;
import com.g4.enums.Department;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.StatusMismatchException;
import com.g4.repository.FleetRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<FleetRentalDTO> getAllRentals() {//Staff

        List<FleetRentalDTO> allRentalDTO = new ArrayList<>();

        fleetRentalRepository
                .findAll()
                .forEach(fleetRental -> allRentalDTO.add(new FleetRentalDTO(fleetRental)));

        return allRentalDTO;
    }

    public FleetRentalDTO findFleetRentalById(Long id) {//Staff

        FleetRental fleetRental = fleetRentalRepository.
                findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fleet rental not found with this id : " + id));

        return new FleetRentalDTO(fleetRental);
    }

    public List<FleetRentalDTO> getCorpRentalByPhoneNum(String phoneNum) {//CorpCustomer

        List<FleetRentalDTO> allRentalDTO = new ArrayList<>();

        fleetRentalRepository
                .findAll()
                .stream()
                .filter(fleetRental -> fleetRental.getCustomer().getPhoneNumber().equals(phoneNum))
                .forEach(fleetRental -> allRentalDTO.add(new FleetRentalDTO(fleetRental)));

        return allRentalDTO;
    }

    public FleetRentalDTO createFleetRental(FleetRentalDTO fleetRentalDTO) {//Staff

        if (fleetRentalDTO.getCarList().size() == 0) {
            throw new ResourceNotFoundException("Cannot operate with empty list");
        }

        CustomerStatus customerStatus = fleetRentalDTO.getCustomer().getStatus();
        if (customerStatus.equals(CustomerStatus.SUSPENDED) || customerStatus.equals(CustomerStatus.TERMINATED)) {
            throw new StatusMismatchException("Customer is not available for rental");
        }

        FleetRental fleetRental = new FleetRental();

        fleetRental.setStatus(fleetRentalDTO.getStatus());
        fleetRental.setAgreement(fleetRentalDTO.getAgreement());
        fleetRental.setReturnDate(fleetRentalDTO.getReturnDate());

        List<Car> carList = new ArrayList<>();
        fleetRentalDTO
                .getCarList()
                .forEach(carDTO -> carList.add(carService.findCar(carDTO.getPlateNumber())));

        fleetRental.setCarList(carList);

        fleetRental.setTotalPrice(fleetRentalDTO.getTotalPrice());
        fleetRental.setCustomer(corporateCustomerService.findCorporate(fleetRentalDTO.getCustomer().getPhoneNumber()));
        fleetRental.setAgreement(fleetRentalDTO.getAgreement());

        fleetRentalRepository.save(fleetRental);

        fleetRentalDTO
                .getCarList()
                .forEach(carDTO -> {
                    carDTO.setStatus(CarStatus.RENTED);
                    carService.updateCar(carDTO.getPlateNumber(), carDTO);
                });

        corporateCustomerService
                .updateCorporateCustomer(fleetRentalDTO.getCustomer().getPhoneNumber(),
                       fleetRentalDTO.getCustomer());

        return new FleetRentalDTO(fleetRental);
    }

    public void returnRental(Long id) {

    }
}
