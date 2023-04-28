package com.g4.domain;

import com.g4.domain.abstracts.Customer;
import com.g4.enums.CustomerStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "corporate_customer")
public class CorporateCustomer extends Customer {
    public CorporateCustomer(Long id, @NotBlank(message = "Name cannot be empty") String name, @NotBlank(message = "Lastname cannot be empty") String lastname, String phoneNumber, String username, String password) {
        super(id, name, lastname, phoneNumber, username, password);
    }

    @NotBlank(message = "Company name cannot be empty")
    @Column(length = 100, nullable = false)
    private String companyName;

    @Email(message = "Provide valid email")
    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @NotNull(message = "Status should be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status = CustomerStatus.getDefault();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FleetRental> fleetRentalList = new ArrayList<>();

}
