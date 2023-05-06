package com.g4.service;

import com.g4.domain.Employee;
import com.g4.dto.EmployeeDTO;
import com.g4.dto.EmployeeLoginDTO;
import com.g4.enums.Position;
import com.g4.exception.IllegalLoginRequestException;
import com.g4.exception.ResourceNotFoundException;
import com.g4.exception.UniqueValueAlreadyExistException;
import com.g4.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Employee createEmployee(EmployeeDTO employeeDTO) {

        boolean existsByUsername = employeeRepository.existsByUsername(employeeDTO.getUsername());
        boolean existsByPhoneNumber = employeeRepository.existsByPhoneNumber(employeeDTO.getPhoneNumber());

        if (existsByUsername) {
            throw new UniqueValueAlreadyExistException("Username is already registered");
        } else if (existsByPhoneNumber) {
            throw new UniqueValueAlreadyExistException("This phone number is already registered");
        }

        Employee employee = new Employee();

        employee.setName(employeeDTO.getName());
        employee.setLastname(employeeDTO.getLastname());
        employee.setBirthdate(employeeDTO.getBirthdate());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setUsername(employeeDTO.getUsername());
        employee.setPassword(employeeDTO.getPassword());
        employee.setPosition(employeeDTO.getPosition());
        employee.setAddress(employeeDTO.getAddress());

        return employeeRepository.save(employee);


    }

    public Employee findEmployee(String phoneNumber) {

        return employeeRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with this phone number not found : " + phoneNumber));

    }

    public EmployeeDTO getEmployeeByPhoneNum(String phoneNumber) {

        return new EmployeeDTO(findEmployee(phoneNumber));
    }

    public void updateEmployee(String phoneNumber, EmployeeDTO employeeDTO) {

        Employee employee = employeeRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Personal customer not found with this phone number : " + phoneNumber));

        if (!employee.getUsername().equals(employeeDTO.getUsername())
                && employeeRepository.findByUsername(employeeDTO.getUsername()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Username already exists");
        }

        if (!employee.getPhoneNumber().equals(employeeDTO.getPhoneNumber())
                && employeeRepository.findByPhoneNumber(employeeDTO.getPhoneNumber()).isPresent()) {
            throw new UniqueValueAlreadyExistException("Phone number already exists");
        }

        employee.setName(employeeDTO.getLastname());
        employee.setLastname(employeeDTO.getLastname());
        employee.setBirthdate(employeeDTO.getBirthdate());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setUsername(employeeDTO.getUsername());
        employee.setPassword(employeeDTO.getPassword());
        employee.setAddress(employeeDTO.getAddress());
        employee.setPosition(employeeDTO.getPosition());

        employeeRepository.save(employee);

    }

    public void deleteEmployee(String phoneNumber) {
        Employee found = findEmployee(phoneNumber);
        found.setPosition(Position.INACTIVE);
        updateEmployee(phoneNumber, new EmployeeDTO(found));
    }

    public Map<String,String> getEmployeeByUsername(EmployeeLoginDTO loginDTO) {

        Employee employee = employeeRepository.
                findByUsername(loginDTO.getUsername()).
                orElseThrow(() -> new ResourceNotFoundException("Employee with this username not found : " + loginDTO.getUsername()));

        if (!employee.getPassword().equals(loginDTO.getPassword())) {
            throw new IllegalLoginRequestException("Failed to login. Username and/or password incorrect");
        }

        Map<String,String> response = new HashMap<>();
        response.put("fullname",employee.getName() + " " + employee.getLastname());
        response.put("phoneNumber",employee.getPhoneNumber());

        return response;

    }
}
