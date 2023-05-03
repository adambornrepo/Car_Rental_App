package com.g4.dto;

import com.g4.domain.PersonalCustomer;
import com.g4.enums.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalCustomerDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Lastname cannot be empty")
    private String lastname;

    private String phoneNumber;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Birthdate year cannot be empty")
    private LocalDate birthdate;

    @NotNull(message = "License year cannot be empty")
    private LocalDate licenseYear;

    @NotNull(message = "Status should be selected")
    private CustomerStatus status;

    public PersonalCustomerDTO(PersonalCustomer personalCustomer) {
        this.name = personalCustomer.getName();
        this.lastname = personalCustomer.getLastname();
        this.phoneNumber = personalCustomer.getPhoneNumber();
        this.username = personalCustomer.getUsername();
        this.password = personalCustomer.getPassword();
        this.birthdate = personalCustomer.getBirthdate();
        this.licenseYear = personalCustomer.getLicenseYear();
        this.status = personalCustomer.getStatus();
    }
}
