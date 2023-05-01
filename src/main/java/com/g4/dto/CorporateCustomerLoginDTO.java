package com.g4.dto;

import com.g4.domain.CorporateCustomer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorporateCustomerLoginDTO {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;


    public CorporateCustomerLoginDTO(CorporateCustomer corporateCustomer) {
        this.username = corporateCustomer.getUsername();
        this.password = corporateCustomer.getPassword();
    }
}
