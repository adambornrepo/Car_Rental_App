package com.g4.domain;

import com.g4.domain.abstracts.Customer;
import com.g4.enums.CustomerStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personal_customer")
public class PersonalCustomer extends Customer {
    public PersonalCustomer(Long id, @NotBlank(message = "Name cannot be empty") String name, @NotBlank(message = "Lastname cannot be empty") String lastname, String phoneNumber, String username, String password) {
        super(id, name, lastname, phoneNumber, username, password);
    }

    @Transient
    private Integer age;

    @NotNull(message = "Birthdate should be selected")
    @Column(nullable = false)
    private LocalDate birthdate;

    @NotNull(message = "LicenseYear should be selected")
    @Column(nullable = false)
    private LocalDate licenseYear;

    @NotNull(message = "Status should be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status = CustomerStatus.getDefault();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PersonalRental> personalRentalList = new ArrayList<>();


}
