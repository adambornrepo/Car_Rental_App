package com.g4.service;

import com.g4.domain.CorporateCustomer;
import com.g4.dto.CorporateCustomerDTO;
import com.g4.dto.CorporateCustomerLoginDTO;
import com.g4.enums.CustomerStatus;
import com.g4.exception.IllegalLoginRequestException;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.UniqueValueAlreadyExistException;
import com.g4.repository.CorporateCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CorporateCustomerService {

    private CorporateCustomerRepository corporateCustomerRepository;

    @Autowired
    public CorporateCustomerService(CorporateCustomerRepository corporateCustomerRepository) {
        this.corporateCustomerRepository = corporateCustomerRepository;
    }

    public CorporateCustomer createCorporateCustomer(CorporateCustomerDTO corporateCustomerDTO) {

        boolean existsByUsername = corporateCustomerRepository.existsByUsername(corporateCustomerDTO.getUsername());
        boolean existsByPhoneNumber = corporateCustomerRepository.existsByPhoneNumber(corporateCustomerDTO.getPhoneNumber());
        boolean existsByEmail = corporateCustomerRepository.existsByEmail(corporateCustomerDTO.getEmail());

        if (existsByUsername) {
            throw new UniqueValueAlreadyExistException("Username is already registered");
        } else if (existsByPhoneNumber) {
            throw new UniqueValueAlreadyExistException("This phone number is already registered");
        } else if (existsByEmail) {
            throw new UniqueValueAlreadyExistException("This email is already registered");
        }

        CorporateCustomer corporateCustomer = new CorporateCustomer();

        corporateCustomer.setCompanyName(corporateCustomerDTO.getCompanyName());
        corporateCustomer.setName(corporateCustomerDTO.getName());
        corporateCustomer.setLastname(corporateCustomerDTO.getLastname());
        corporateCustomer.setUsername(corporateCustomerDTO.getUsername());
        corporateCustomer.setPassword(corporateCustomerDTO.getPassword());
        corporateCustomer.setEmail(corporateCustomerDTO.getEmail());
        corporateCustomer.setPhoneNumber(corporateCustomerDTO.getPhoneNumber());

        return corporateCustomerRepository.save(corporateCustomer);
    }

    public CorporateCustomer findCorporate(String phoneNumber) {

        return corporateCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Corporate customer with this phone number not found : " + phoneNumber));
    }

    public CorporateCustomerDTO findCorporateCustomerByPhoneNumber(String phoneNumber) {

        CorporateCustomer corporateCustomer = corporateCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Corporate customer with this phone number not found : " + phoneNumber));

        return new CorporateCustomerDTO(corporateCustomer);
    }

    public void updateCorporateCustomer(String phoneNumber, CorporateCustomerDTO corporateCustomerDTO) {

        CorporateCustomer corporateCustomer = corporateCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Corporate customer not found with this phone number : " + phoneNumber));

        if (!corporateCustomer.getUsername().equals(corporateCustomerDTO.getUsername())
                && corporateCustomerRepository.findByUsername(corporateCustomerDTO.getUsername()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Username already exists");
        }

        if (!corporateCustomer.getPhoneNumber().equals(corporateCustomerDTO.getPhoneNumber())
                && corporateCustomerRepository.findByPhoneNumber(corporateCustomerDTO.getPhoneNumber()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Phone number already exists");
        }

        if (!corporateCustomer.getEmail().equals(corporateCustomerDTO.getEmail())
                && corporateCustomerRepository.findByEmail(corporateCustomerDTO.getEmail()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Email already exists");
        }

        corporateCustomer.setCompanyName(corporateCustomerDTO.getCompanyName());
        corporateCustomer.setName(corporateCustomerDTO.getName());
        corporateCustomer.setLastname(corporateCustomerDTO.getLastname());
        corporateCustomer.setUsername(corporateCustomerDTO.getUsername());
        corporateCustomer.setPassword(corporateCustomerDTO.getPassword());
        corporateCustomer.setEmail(corporateCustomerDTO.getEmail());
        corporateCustomer.setPhoneNumber(corporateCustomerDTO.getPhoneNumber());
        corporateCustomer.setStatus(corporateCustomerDTO.getStatus());

        corporateCustomerRepository.save(corporateCustomer);

    }

    public void deleteCorporateCustomer(String phoneNumber) {
        CorporateCustomerDTO found = findCorporateCustomerByPhoneNumber(phoneNumber);
        found.setStatus(CustomerStatus.TERMINATED);
        updateCorporateCustomer(phoneNumber, found);
    }

    public List<CorporateCustomerDTO> getAllCorporateCustomer() {
        List<CorporateCustomerDTO> allCorporateCustomer = new ArrayList<>();

        corporateCustomerRepository
                .findAll()
                .forEach(corporateCustomer -> allCorporateCustomer.add(new CorporateCustomerDTO(corporateCustomer)));

        return allCorporateCustomer;
    }

    public List<CorporateCustomerDTO> getAllActiveCorporateCustomer() {
        List<CorporateCustomerDTO> allCorporateCustomer = new ArrayList<>();

        corporateCustomerRepository
                .findAllActive(CustomerStatus.AVAILABLE, CustomerStatus.HAS_RENTED)
                .forEach(corporateCustomer -> allCorporateCustomer.add(new CorporateCustomerDTO(corporateCustomer)));

        return allCorporateCustomer;
    }

    public Map<String,String> getCorporateCustomerByUsername(CorporateCustomerLoginDTO loginDTO) {

        CorporateCustomer corporateCustomer = corporateCustomerRepository.
                findByUsername(loginDTO.getUsername()).
                orElseThrow(() -> new ResourceNotFoundException("Corporate customer with this username not found : " + loginDTO.getUsername()));

        if (!corporateCustomer.getPassword().equals(loginDTO.getPassword())) {
            throw new IllegalLoginRequestException("Failed to login. Username and/or password incorrect");
        }

        Map<String,String> response = new HashMap<>();
        response.put("fullname",corporateCustomer.getName() + " " + corporateCustomer.getLastname());
        response.put("phoneNumber",corporateCustomer.getPhoneNumber());

        return response;
    }


}
