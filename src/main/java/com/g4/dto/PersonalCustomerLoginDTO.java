package com.g4.dto;

import com.g4.domain.PersonalCustomer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalCustomerLoginDTO {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;


    public PersonalCustomerLoginDTO(PersonalCustomer personalCustomer) {
        this.username = personalCustomer.getUsername();
        this.password = personalCustomer.getPassword();
    }
}
