package com.g4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.g4.domain.Address;
import com.g4.domain.Employee;
import com.g4.enums.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {


    private Long id;

    @NotBlank(message = "Employee name cannot be empty")
    private String name;

    @NotBlank(message = "Employee lastname cannot be empty")
    private String lastname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Birthdate cannot be null")
    private LocalDate birthdate;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    private Address address;

    @NotBlank(message = "Username cannot be white space or empty")
    private String username;

    @NotBlank(message = "Password cannot be white space or empty")
    private String password;

    @NotNull(message = "Position should be selected")
    private Position position;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.lastname = employee.getLastname();
        this.birthdate = employee.getBirthdate();
        this.phoneNumber = employee.getPhoneNumber();
        this.address = employee.getAddress();
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.position = employee.getPosition();
    }
}
