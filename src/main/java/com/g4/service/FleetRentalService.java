package com.g4.service;

import com.g4.domain.Car;
import com.g4.domain.CorporateCustomer;
import com.g4.domain.FleetRental;
import com.g4.domain.PersonalRental;
import com.g4.dto.*;
import com.g4.enums.CarStatus;
import com.g4.enums.CustomerStatus;
import com.g4.enums.Department;
import com.g4.enums.RentalStatus;
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

    public FleetRentalDTO createFleetRental(FleetRentalCreateDTO fleetRentalCreateDTO) {//Staff

        if (fleetRentalCreateDTO.getCarPlateNumList().size() == 0) {
            throw new ResourceNotFoundException("Cannot operate with empty list");
        }

        CustomerStatus customerStatus = corporateCustomerService.findCorporate(fleetRentalCreateDTO.getCustomerPhoneNum()).getStatus();
        if (customerStatus.equals(CustomerStatus.SUSPENDED) || customerStatus.equals(CustomerStatus.TERMINATED)) {
            throw new StatusMismatchException("Customer is not available for rental");
        }

        FleetRental fleetRental = new FleetRental();

        fleetRental.setStatus(fleetRentalCreateDTO.getStatus());
        fleetRental.setAgreement(fleetRentalCreateDTO.getAgreement());
        fleetRental.setReturnDate(fleetRentalCreateDTO.getReturnDate());

        List<Car> carList = new ArrayList<>();
        fleetRentalCreateDTO
                .getCarPlateNumList()
                .forEach(plateNum -> {
                    carList.add(carService.findCar(plateNum));
                });

        fleetRental.setCarList(carList);

        fleetRental.setTotalPrice(fleetRentalCreateDTO.getTotalPrice());
        fleetRental.setCustomer(corporateCustomerService.findCorporate(fleetRentalCreateDTO.getCustomerPhoneNum()));
        fleetRental.setAgreement(fleetRentalCreateDTO.getAgreement());

        fleetRentalRepository.save(fleetRental);

        fleetRentalCreateDTO
                .getCarPlateNumList()
                .forEach(plateNum -> {
                    CarDTO carDTO = carService.findCarByPlateNumber(plateNum);
                    carDTO.setStatus(CarStatus.RENTED);
                    carService.updateCar(carDTO.getPlateNumber(), carDTO);
                });

        corporateCustomerService
                .updateCorporateCustomer(fleetRentalCreateDTO.getCustomerPhoneNum(),
                        corporateCustomerService.findCorporateCustomerByPhoneNumber(fleetRentalCreateDTO.getCustomerPhoneNum()));

        return new FleetRentalDTO(fleetRental);
    }

    public void returnRental(Long id) {
        FleetRental fleetRental = fleetRentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fleet rental not found with this id : " + id));
        fleetRental.setStatus(RentalStatus.COMPLETED);
        fleetRentalRepository.save(fleetRental);

        fleetRental.getCarList().forEach(car -> {
            CarDTO carDTO = carService.findCarByPlateNumber(car.getPlateNumber());
            carDTO.setStatus(CarStatus.AVAILABLE);
            carService.updateCar(car.getPlateNumber(), carDTO);
        });
    }
}
