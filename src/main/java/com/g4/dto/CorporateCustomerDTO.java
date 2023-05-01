package com.g4.dto;

import com.g4.domain.CorporateCustomer;
import com.g4.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorporateCustomerDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Lastname cannot be empty")
    private String lastname;

    private String phoneNumber;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Company name cannot be empty")
    private String companyName;

    @Email(message = "Provide valid email")
    private String email;

    @NotNull(message = "Status should be selected")
    private CustomerStatus status;


    public CorporateCustomerDTO(CorporateCustomer corporateCustomer) {
        this.name = corporateCustomer.getName();
        this.lastname = corporateCustomer.getLastname();
        this.phoneNumber = corporateCustomer.getPhoneNumber();
        this.username = corporateCustomer.getUsername();
        this.password = corporateCustomer.getPassword();
        this.companyName = corporateCustomer.getCompanyName();
        this.email = corporateCustomer.getEmail();
        this.status = corporateCustomer.getStatus();
    }
}
