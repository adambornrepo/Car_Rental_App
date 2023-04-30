package com.g4.dto;

import com.g4.domain.PersonalRental;
import com.g4.enums.RentalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalRentalDTO {

    private Long id;

    @NotNull(message = "Car must be provided for rental process")
    private CarDTO car;

    @NotNull(message = "Customer must be provided for rental process")
    private PersonalCustomerDTO customer;

    @NotNull(message = "Return date must be provided")
    private LocalDate returnDate;

    @NotNull(message = "Status should be selected")
    private RentalStatus status;

    public PersonalRentalDTO(PersonalRental personalRental) {
        this.id = personalRental.getId();
        this.car = new CarDTO(personalRental.getCar());
        this.customer = new PersonalCustomerDTO(personalRental.getCustomer());
        this.returnDate = personalRental.getReturnDate();
        this.status = personalRental.getStatus();
    }

}
