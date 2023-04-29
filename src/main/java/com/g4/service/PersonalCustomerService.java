package com.g4.service;

import com.g4.domain.PersonalCustomer;
import com.g4.dto.PersonalCustomerDTO;
import com.g4.enums.CustomerStatus;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.UniqueValueAlreadyExistException;
import com.g4.repository.PersonalCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        }


        PersonalCustomer personalCustomer = new PersonalCustomer();

        personalCustomer.setName(personalCustomerDTO.getName());
        personalCustomer.setLastname(personalCustomerDTO.getLastname());
        personalCustomer.setPhoneNumber(personalCustomerDTO.getPhoneNumber());
        personalCustomer.setUsername(personalCustomerDTO.getUsername());
        personalCustomer.setPassword(personalCustomerDTO.getPassword());
        personalCustomer.setLicenseYear(personalCustomerDTO.getLicenseYear());

        return personalCustomerRepository.save(personalCustomer);
    }

    public PersonalCustomerDTO findPersonalCustomerById(Long id) {
        PersonalCustomer personalCustomer = personalCustomerRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Personal customer with this ID not found :" + id));

        PersonalCustomerDTO found = new PersonalCustomerDTO(personalCustomer);
        return found;
    }


    public void updatePersonalCustomer(Long id, PersonalCustomerDTO personalCustomerDTO) {

        PersonalCustomer personalCustomer = personalCustomerRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Personal customer not found with id: " + id));

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
        personalCustomer.setLicenseYear(personalCustomerDTO.getLicenseYear());
        personalCustomer.setStatus(personalCustomerDTO.getStatus());

        personalCustomerRepository.save(personalCustomer);

    }

    public void deletePersonalCustomer(Long id) {

        PersonalCustomerDTO found = findPersonalCustomerById(id);
        found.setStatus(CustomerStatus.TERMINATED);
        updatePersonalCustomer(id, found);

    }
}
