package com.g4.service;

import com.g4.domain.PersonalCustomer;
import com.g4.dto.PersonalCustomerDTO;
import com.g4.dto.PersonalCustomerLoginDTO;
import com.g4.enums.CustomerStatus;
import com.g4.exception.IllegalInputException;
import com.g4.exception.IllegalLoginRequestException;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.UniqueValueAlreadyExistException;
import com.g4.repository.PersonalCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonalCustomerService {

    private PersonalCustomerRepository personalCustomerRepository;

    @Autowired
    public PersonalCustomerService(PersonalCustomerRepository personalCustomerRepository) {
        this.personalCustomerRepository = personalCustomerRepository;
    }

    public PersonalCustomer createPersonalCustomer(PersonalCustomerDTO personalCustomerDTO) {

        boolean existsByUsername = personalCustomerRepository.existsByUsername(personalCustomerDTO.getUsername());
        boolean existsByPhoneNumber = personalCustomerRepository.existsByPhoneNumber(personalCustomerDTO.getPhoneNumber());

        if (existsByUsername) {
            throw new UniqueValueAlreadyExistException("Username is already registered");
        } else if (existsByPhoneNumber) {
            throw new UniqueValueAlreadyExistException("This phone number is already registered");
        } else if (personalCustomerDTO.getAge() < 20) {
            throw new IllegalInputException("Personal customer cannot be under 20 years old");
        } else if (!personalCustomerDTO.isValidLicenseYear()) {
            throw  new IllegalInputException("License must be at least two years");
        }


        PersonalCustomer personalCustomer = new PersonalCustomer();

        personalCustomer.setName(personalCustomerDTO.getName());
        personalCustomer.setLastname(personalCustomerDTO.getLastname());
        personalCustomer.setPhoneNumber(personalCustomerDTO.getPhoneNumber());
        personalCustomer.setUsername(personalCustomerDTO.getUsername());
        personalCustomer.setPassword(personalCustomerDTO.getPassword());
        personalCustomer.setBirthdate(personalCustomerDTO.getBirthdate());
        personalCustomer.setLicenseYear(personalCustomerDTO.getLicenseYear());
        return personalCustomerRepository.save(personalCustomer);
    }

// Not used anymore
//    public PersonalCustomerDTO findPersonalCustomerById(Long id) {
//        PersonalCustomer personalCustomer = personalCustomerRepository.
//                findById(id).
//                orElseThrow(() -> new ResourceNotFoundException("Personal customer with this ID not found : " + id));
//
//        return new PersonalCustomerDTO(personalCustomer);
//    }

    public PersonalCustomer findPersonal(String phoneNumber) {

        return personalCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Personal customer with this phone number not found : " + phoneNumber));

    }


    public void updatePersonalCustomer(String phoneNumber, PersonalCustomerDTO personalCustomerDTO) {

        PersonalCustomer personalCustomer = personalCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Personal customer not found with this phone number : " + phoneNumber));

        if (!personalCustomer.getUsername().equals(personalCustomerDTO.getUsername())
                && personalCustomerRepository.findByUsername(personalCustomerDTO.getUsername()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Username already exists");
        }

        if (!personalCustomer.getPhoneNumber().equals(personalCustomerDTO.getPhoneNumber())
                && personalCustomerRepository.findByPhoneNumber(personalCustomerDTO.getPhoneNumber()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Phone number already exists");
        }

        personalCustomer.setName(personalCustomerDTO.getName());
        personalCustomer.setLastname(personalCustomerDTO.getLastname());
        personalCustomer.setPhoneNumber(personalCustomerDTO.getPhoneNumber());
        personalCustomer.setUsername(personalCustomerDTO.getUsername());
        personalCustomer.setPassword(personalCustomerDTO.getPassword());
        personalCustomer.setBirthdate(personalCustomerDTO.getBirthdate());
        personalCustomer.setLicenseYear(personalCustomerDTO.getLicenseYear());
        personalCustomer.setStatus(personalCustomerDTO.getStatus());

        personalCustomerRepository.save(personalCustomer);

    }

    public void deletePersonalCustomer(String phoneNumber) {

        PersonalCustomerDTO found = findPersonalCustomerByPhoneNumber(phoneNumber);
        found.setStatus(CustomerStatus.TERMINATED);
        updatePersonalCustomer(phoneNumber, found);

    }

    public PersonalCustomerDTO findPersonalCustomerByPhoneNumber(String phoneNumber) {

        PersonalCustomer personalCustomer = personalCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Personal customer with this phone number not found :" + phoneNumber));

        return new PersonalCustomerDTO(personalCustomer);

    }

    public List<PersonalCustomerDTO> getAllPersonalCustomer() {
        List<PersonalCustomerDTO> allPersonalCustomer = new ArrayList<>();

        personalCustomerRepository
                .findAll()
                .forEach(personalCustomer -> allPersonalCustomer.add(new PersonalCustomerDTO(personalCustomer)));

        return allPersonalCustomer;
    }


    public Map<String,String> getPersonalCustomerByUsername(PersonalCustomerLoginDTO loginDTO) {
        PersonalCustomer personalCustomer = personalCustomerRepository.
                findByUsername(loginDTO.getUsername()).
                orElseThrow(() -> new ResourceNotFoundException("Personal customer with this username not found : " + loginDTO.getUsername()));

        if (!personalCustomer.getPassword().equals(loginDTO.getPassword())) {
            throw new IllegalLoginRequestException("Failed to login. Username and/or password incorrect");
        }
        Map<String,String> resp = new HashMap<>();
        resp.put("fullname",personalCustomer.getName() + " " + personalCustomer.getLastname());
        resp.put("phoneNumber",personalCustomer.getPhoneNumber());

        return resp;
    }

    public List<PersonalCustomerDTO> getAllActivePersonalCustomer() {

        List<PersonalCustomerDTO> allActivePersonalCustomer = new ArrayList<>();

        personalCustomerRepository
                .findAllActive(CustomerStatus.AVAILABLE, CustomerStatus.HAS_RENTED)
                .forEach(personalCustomer -> allActivePersonalCustomer.add(new PersonalCustomerDTO(personalCustomer)));

        return allActivePersonalCustomer;
    }
}
