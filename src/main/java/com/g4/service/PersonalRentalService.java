package com.g4.service;

import com.g4.domain.PersonalRental;
import com.g4.dto.CarDTO;
import com.g4.dto.PerRentalCreateUpdateDTO;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    Logger logger = Logger.getLogger(PersonalRentalService.class.getName());

    public List<PersonalRentalDTO> getAllRentals() {//For staff

        List<PersonalRentalDTO> allRentalDTO = new ArrayList<>();
        personalRentalRepository
                .findAll()
                .forEach(personalRental -> allRentalDTO.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTO;
    }

    public List<PersonalRentalDTO> getAllOngoingRentals() {// For staff
        List<PersonalRentalDTO> allRentalDTO = new ArrayList<>();

        personalRentalRepository
                .findAllOngoingRentals(RentalStatus.RESERVED, RentalStatus.RENTED)
                .forEach(personalRental -> allRentalDTO.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTO;
    }

    public PersonalRentalDTO findPersonalRentalById(Long id) {//PerCustomer

        PersonalRental personalRental = personalRentalRepository.
                findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this id : " + id));

        return new PersonalRentalDTO(personalRental);
    }

    public List<PersonalRentalDTO> getPersonalRentalByPerCustomerPhoneNum(String phoneNum) {//PerCustomer

        return getAllOngoingRentals()
                .stream()
                .filter(personalRentalDTO -> personalRentalDTO.getCustomer().getPhoneNumber().equals(phoneNum))
                .toList();
    }

    public List<PersonalRentalDTO> getAllPersonalRentalByPerCustomerPhoneNum(String phoneNum) {//PerCustomer - Staff

        List<PersonalRentalDTO> allRentalDTO = new ArrayList<>();

        personalRentalRepository
                .findAll()
                .stream()
                .filter(personalRental -> personalRental.getCustomer().getPhoneNumber().equals(phoneNum))
                .forEach(personalRental -> allRentalDTO.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTO;
    }


    public List<PersonalRentalDTO> getPersonalRentalByReturnDate(LocalDate returnDate) {//Staff

        List<PersonalRentalDTO> allRentalDTOReturnDate = new ArrayList<>();

        personalRentalRepository
                .findAllByReturnDate(returnDate)
                .forEach(personalRental -> allRentalDTOReturnDate.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTOReturnDate;
    }


    public PersonalRentalDTO createPersonalRental(PerRentalCreateUpdateDTO perRentalCreateUpdateDTO) {//PerCustomer

        CarDTO car = new CarDTO(carService.findCar(perRentalCreateUpdateDTO.getCarPlateNum()));
        PersonalCustomerDTO personalCustomer = new PersonalCustomerDTO(personalCustomerService.findPersonal(perRentalCreateUpdateDTO.getCustomerPhoneNum()));

        if (!car.getStatus().equals(CarStatus.AVAILABLE)) {
            throw new StatusMismatchException("Car is not available for rental");
        }

        if (!personalCustomer.getStatus().equals(CustomerStatus.AVAILABLE)) {
            throw new StatusMismatchException("Customer is not available for rental");
        }

        PersonalRental personalRental = new PersonalRental();

        personalRental.setCar(carService.findCar(car.getPlateNumber()));
        personalRental.setCustomer(personalCustomerService.findPersonal(personalCustomer.getPhoneNumber()));
        personalRental.setReturnDate(perRentalCreateUpdateDTO.getReturnDate());
        personalRental.setStatus(perRentalCreateUpdateDTO.getStatus());

        personalRentalRepository.save(personalRental);

        if (perRentalCreateUpdateDTO.getStatus().equals(RentalStatus.RENTED)) {
            car.setStatus(CarStatus.RENTED);
        } else if (perRentalCreateUpdateDTO.getStatus().equals(RentalStatus.RESERVED)) {
            car.setStatus(CarStatus.RESERVED);
        } else {
            throw new StatusMismatchException("This status cannot be used when creating a rental : " + perRentalCreateUpdateDTO.getStatus());
        }


        carService.updateCar(car.getPlateNumber(), car);

        personalCustomer.setStatus(CustomerStatus.HAS_RENTED);
        personalCustomerService.updatePersonalCustomer(personalCustomer.getPhoneNumber(), personalCustomer);

        return new PersonalRentalDTO(personalRental);

    }

    public void cancelPersonalRental(Long id) {//PerCustomer

        PersonalRentalDTO foundPersonalRental = findPersonalRentalById(id);

        if (foundPersonalRental.getStatus().equals(RentalStatus.RESERVED)) {

            foundPersonalRental.setStatus(RentalStatus.CANCELLED);
            updatePersonalRental(id, foundPersonalRental.toPerRentalCreateUpdateDTO());

            CarDTO foundCar = foundPersonalRental.getCar();
            foundCar.setStatus(CarStatus.AVAILABLE);
            carService.updateCar(foundCar.getPlateNumber(), foundCar);

            PersonalCustomerDTO foundCustomer = foundPersonalRental.getCustomer();
            foundCustomer.setStatus(CustomerStatus.AVAILABLE);
            personalCustomerService.updatePersonalCustomer(foundCustomer.getPhoneNumber(), foundCustomer);


        } else {
            throw new StatusMismatchException("Rental cannot be cancelled. Because the rental : " + foundPersonalRental.getStatus());
        }

    }

    public PersonalRentalDTO updatePersonalRental(Long id, PerRentalCreateUpdateDTO rentalDTO) {//PerCustomer

        PersonalRental personalRental = personalRentalRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this id : " + id));


        CarDTO carUpdated = new CarDTO(carService.findCar(rentalDTO.getCarPlateNum()));
        PersonalCustomerDTO customerUpdate = new PersonalCustomerDTO(personalCustomerService.findPersonal(rentalDTO.getCustomerPhoneNum()));

        CarStatus status = carService.findCarByPlateNumber(carUpdated.getPlateNumber()).getStatus();

        if (!personalRental.getCar().getPlateNumber().equals(carUpdated.getPlateNumber()) && !status.equals(CarStatus.AVAILABLE)) {
            throw new StatusMismatchException("Personal rental cannot be updated with this car. Because car is  : " + status);
        }


        if (!personalRental.getCar().getPlateNumber().equals(carUpdated.getPlateNumber())) {

            carUpdated.setStatus(personalRental.getCar().getStatus());
            carService.updateCar(carUpdated.getPlateNumber(), carUpdated);

            CarDTO car = new CarDTO(personalRental.getCar());
            car.setStatus(CarStatus.AVAILABLE);
            carService.updateCar(car.getPlateNumber(), car);

        }


        personalRental.setCar(carService.findCar(carUpdated.getPlateNumber()));
        personalRental.setCustomer(personalCustomerService.findPersonal(customerUpdate.getPhoneNumber()));
        personalRental.setReturnDate(rentalDTO.getReturnDate());
        personalRental.setReturnDate(rentalDTO.getReturnDate());
        personalRental.setStatus(rentalDTO.getStatus());// FIXME: 5.05.2023

        personalRentalRepository.save(personalRental);

        return new PersonalRentalDTO(personalRental);
    }

    public void returnRental(Long id) {//PerCustomer
        PersonalRentalDTO foundPersonalRental = findPersonalRentalById(id);

        if (foundPersonalRental.getStatus().equals(RentalStatus.RENTED)) {

            foundPersonalRental.setStatus(RentalStatus.COMPLETED);
            updatePersonalRental(id, foundPersonalRental.toPerRentalCreateUpdateDTO());

            CarDTO foundCar = foundPersonalRental.getCar();
            foundCar.setStatus(CarStatus.AVAILABLE);
            carService.updateCar(foundCar.getPlateNumber(), foundCar);

            PersonalCustomerDTO foundCustomer = foundPersonalRental.getCustomer();
            foundCustomer.setStatus(CustomerStatus.AVAILABLE);
            personalCustomerService.updatePersonalCustomer(foundCustomer.getPhoneNumber(), foundCustomer);


        } else {
            throw new StatusMismatchException("Rental cannot be completed. Because the rental : " + foundPersonalRental.getStatus());
        }


    }


}
