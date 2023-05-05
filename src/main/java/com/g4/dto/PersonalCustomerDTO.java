package com.g4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.g4.domain.PersonalCustomer;
import com.g4.enums.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Birthdate year cannot be empty")
    private LocalDate birthdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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

    public Integer getAge() {
        return (int) ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }

    public Boolean isValidLicenseYear() {
        return (int) ChronoUnit.YEARS.between(licenseYear, LocalDate.now()) >= 2;
    }
}
