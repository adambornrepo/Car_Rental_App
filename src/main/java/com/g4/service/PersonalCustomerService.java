package com.g4.service;

import com.g4.domain.PersonalCustomer;
import com.g4.dto.PersonalCustomerRegistryDTO;
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

    public PersonalCustomer createPersonalCustomer(PersonalCustomerRegistryDTO personalCustomerRegistryDTO) {

        boolean existsByUsername = personalCustomerRepository.existsByUsername(personalCustomerRegistryDTO.getUsername());
        boolean existsByPhoneNumber = personalCustomerRepository.existsByPhoneNumber(personalCustomerRegistryDTO.getPhoneNumber());

        if (existsByUsername) {
            throw new UniqueValueAlreadyExistException("Username is already registered");
        } else if (existsByPhoneNumber) {
            throw new UniqueValueAlreadyExistException("This phone number is already registered");
        }


        PersonalCustomer personalCustomer = new PersonalCustomer();
        personalCustomer.setName(personalCustomerRegistryDTO.getName());
        personalCustomer.setLastname(personalCustomerRegistryDTO.getLastname());
        personalCustomer.setPhoneNumber(personalCustomerRegistryDTO.getPhoneNumber());
        personalCustomer.setUsername(personalCustomerRegistryDTO.getUsername());
        personalCustomer.setPassword(personalCustomerRegistryDTO.getPassword());
        personalCustomer.setLicenseYear(personalCustomerRegistryDTO.getLicenseYear());

        return personalCustomerRepository.save(personalCustomer);
    }
}
