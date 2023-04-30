package com.g4.service;

import com.g4.domain.PersonalRental;
import com.g4.dto.CarDTO;
import com.g4.dto.PersonalCustomerDTO;
import com.g4.dto.PersonalRentalDTO;
import com.g4.enums.CarStatus;
import com.g4.enums.CustomerStatus;
import com.g4.enums.RentalStatus;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.StatusMismatchException;
import com.g4.repository.PersonalRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalRentalService {

    private CarService carService;

    private PersonalCustomerService personalCustomerService;

    private PersonalRentalRepository personalRentalRepository;

    @Autowired
    public PersonalRentalService(CarService carService, PersonalCustomerService personalCustomerService, PersonalRentalRepository personalRentalRepository) {
        this.carService = carService;
        this.personalCustomerService = personalCustomerService;
        this.personalRentalRepository = personalRentalRepository;
    }


    public PersonalRentalDTO createPersonalRental(PersonalRentalDTO personalRentalDTO) {

        CarDTO car = personalRentalDTO.getCar();
        PersonalCustomerDTO personalCustomer = personalRentalDTO.getCustomer();

        if (car == null || personalCustomer == null) {
            throw new ResourceNotFoundException("All fields must be provided");
        }

        if (!car.getStatus().equals(CarStatus.AVAILABLE)) {
            throw new StatusMismatchException("Car is not available for rental");
        }

        if (!personalCustomer.getStatus().equals(CustomerStatus.AVAILABLE)) {
            throw new StatusMismatchException("Customer is not available for rental");
        }

        PersonalRental personalRental = new PersonalRental();

        personalRental.setCar(carService.findCar(car.getPlateNumber()));
        personalRental.setCustomer(personalCustomerService.findPersonal(personalCustomer.getPhoneNumber()));
        personalRental.setReturnDate(personalRentalDTO.getReturnDate());
        personalRental.setStatus(personalRentalDTO.getStatus());

        personalRentalRepository.save(personalRental);

        car.setStatus(CarStatus.RENTED);
        carService.updateCar(car.getPlateNumber(), car);//update methodu yazılacak

        personalCustomer.setStatus(CustomerStatus.HAS_RENTED);
        personalCustomerService.updatePersonalCustomer(personalCustomer.getPhoneNumber(), personalCustomer);

        return new PersonalRentalDTO(personalRental);

    }

    public void cancelPersonalRental(Long id) {

        PersonalRentalDTO foundPersonalRental = findPersonalRentalById(id);
        foundPersonalRental.setStatus(RentalStatus.CANCELLED);
        updatePersonalRental(id, foundPersonalRental);


    }

    public PersonalRentalDTO updatePersonalRental(Long id, PersonalRentalDTO rentalDTO) {

        PersonalRental personalRental = personalRentalRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this id : " + id));

        CarDTO carUpdate = rentalDTO.getCar();
        PersonalCustomerDTO customerUpdate = rentalDTO.getCustomer();

        CarStatus status = carService.findCarByPlateNumber(carUpdate.getPlateNumber()).getStatus();

        if (!personalRental.getCar().getPlateNumber().equals(carUpdate.getPlateNumber())
                && !status.equals("AVAILABLE")) {
            throw new StatusMismatchException("Personal rental cannot be updated with this car. Because car is  : " + status);
        }

        personalRental.setCar(carService.findCar(carUpdate.getPlateNumber()));
        personalRental.setCustomer(personalCustomerService.findPersonal(customerUpdate.getPhoneNumber()));
        personalRental.setReturnDate(rentalDTO.getReturnDate());
        personalRental.setReturnDate(rentalDTO.getReturnDate());

        personalRentalRepository.save(personalRental);

        return new PersonalRentalDTO(personalRental);
    }

    public List<PersonalRentalDTO> getAllRentals() {
        List<PersonalRentalDTO> allRentalDTO = new ArrayList<>();

        personalRentalRepository.
                findAll().stream().filter(personalRental -> personalRental.getStatus().equals("RENTED")).
                forEach(personalRental -> allRentalDTO.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTO;
    }

    public PersonalRentalDTO findPersonalRentalById(Long id) {

        PersonalRental personalRental = personalRentalRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this id : " + id));

        return new PersonalRentalDTO(personalRental);
    }


//    public PersonalRentalDTO findPersonalRentalByCarPlateNum(String plateNumber) {
//
//
//        PersonalRental personalRental = personalRentalRepository.
//                findPersonalRentalByCar(carService.findCar(plateNumber)).
//                orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this plate number : " + plateNumber));
//
//        return new PersonalRentalDTO(personalRental);
//    }
//
//    public PersonalRentalDTO findPersonalRentalByPerCustomerPhoneNum(String phoneNumber) {
//
//        PersonalRental personalRental = personalRentalRepository.
//                findPersonalRentalByPersonalCustomer(personalCustomerService.findPersonal(phoneNumber)).
//                orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this phone number : " + phoneNumber));
//
//        return new PersonalRentalDTO(personalRental);
//    }

}
