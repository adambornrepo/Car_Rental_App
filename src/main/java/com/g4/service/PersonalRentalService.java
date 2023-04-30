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

import java.time.LocalDate;
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

        if (personalRentalDTO.getStatus().equals(RentalStatus.RENTED)) {
            car.setStatus(CarStatus.RENTED);
        } else if (personalRentalDTO.getStatus().equals(RentalStatus.RESERVED)) {
            car.setStatus(CarStatus.RESERVED);
        } else {
            throw new StatusMismatchException("this status cannot be used when creating a rental : " + personalRentalDTO.getStatus());
        }


        carService.updateCar(car.getPlateNumber(), car);

        personalCustomer.setStatus(CustomerStatus.HAS_RENTED);
        personalCustomerService.updatePersonalCustomer(personalCustomer.getPhoneNumber(), personalCustomer);

        return new PersonalRentalDTO(personalRental);

    }

    public void cancelPersonalRental(Long id) {

        PersonalRentalDTO foundPersonalRental = findPersonalRentalById(id);

        if (foundPersonalRental.getStatus().equals(RentalStatus.RESERVED)) {

            foundPersonalRental.setStatus(RentalStatus.CANCELLED);
            updatePersonalRental(id, foundPersonalRental);

        } else {
            throw new StatusMismatchException("Rental cannot be cancelled. Because the rental : " + foundPersonalRental.getStatus());
        }

    }

    public PersonalRentalDTO updatePersonalRental(Long id, PersonalRentalDTO rentalDTO) {

        PersonalRental personalRental = personalRentalRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this id : " + id));

        CarDTO carUpdated = rentalDTO.getCar();
        PersonalCustomerDTO customerUpdate = rentalDTO.getCustomer();

        CarStatus status = carService.findCarByPlateNumber(carUpdated.getPlateNumber()).getStatus();

        if (!personalRental.getCar().getPlateNumber().equals(carUpdated.getPlateNumber()) && !status.equals(CarStatus.AVAILABLE)) {
            throw new StatusMismatchException("Personal rental cannot be updated with this car. Because car is  : " + status);
        }

        if (!personalRental.getCar().getPlateNumber().equals(carUpdated.getPlateNumber())) {

            CarDTO car = new CarDTO(personalRental.getCar());
            car.setStatus(CarStatus.AVAILABLE);
            carService.updateCar(car.getPlateNumber(), car);

            carUpdated.setStatus(CarStatus.RENTED);
            carService.updateCar(carUpdated.getPlateNumber(), carUpdated);
        }

        personalRental.setCar(carService.findCar(carUpdated.getPlateNumber()));
        personalRental.setCustomer(personalCustomerService.findPersonal(customerUpdate.getPhoneNumber()));
        personalRental.setReturnDate(rentalDTO.getReturnDate());
        personalRental.setReturnDate(rentalDTO.getReturnDate());

        personalRentalRepository.save(personalRental);

        return new PersonalRentalDTO(personalRental);
    }

    public List<PersonalRentalDTO> getAllOngoingRentals() {
        List<PersonalRentalDTO> allRentalDTO = new ArrayList<>();

        personalRentalRepository
                .findAll()
                .stream()
                .filter(personalRental -> personalRental.getStatus().equals(RentalStatus.RENTED) || personalRental.getStatus().equals(RentalStatus.RESERVED))
                .forEach(personalRental -> allRentalDTO.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTO;
    }

    public PersonalRentalDTO findPersonalRentalById(Long id) {

        PersonalRental personalRental = personalRentalRepository.
                findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal rental not found with this id : " + id));

        return new PersonalRentalDTO(personalRental);
    }

    public List<PersonalRentalDTO> getPersonalRentalByPerCustomerPhoneNum(String phoneNum) {

        return getAllOngoingRentals()
                .stream()
                .filter(personalRentalDTO -> personalRentalDTO.getCustomer().getPhoneNumber().equals(phoneNum))
                .toList();
    }


    public List<PersonalRentalDTO> getPersonalRentalByReturnDate(LocalDate returnDate) {

        List<PersonalRentalDTO> allRentalDTOReturnDate = new ArrayList<>();

        personalRentalRepository
                .findAllByReturnDate(returnDate)
                .stream()
                .filter(personalRental -> personalRental.getReturnDate().isEqual(returnDate))
                .forEach(personalRental -> allRentalDTOReturnDate.add(new PersonalRentalDTO(personalRental)));

        return allRentalDTOReturnDate;
    }


}
