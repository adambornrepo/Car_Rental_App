package com.g4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g4.domain.Car;
import com.g4.domain.CorporateCustomer;
import com.g4.domain.FleetRental;
import com.g4.enums.RentalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FleetRentalDTO {

    private Long id;

    @NotNull(message = "Car must be provided for rental process")
    private List<CarDTO> carList = new ArrayList<>();

    @NotNull(message = "Customer must be provided for rental process")
    private CorporateCustomerDTO customer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Return date must be provided")
    private LocalDate returnDate;

    @NotNull(message = "Status should be selected")
    private RentalStatus status;

    @Positive(message = "Total price cannot be negative")
    @NotNull(message = "Total price cannot be null")
    private Double totalPrice;

    private byte[] agreement;

    public FleetRentalDTO(FleetRental fleetRental) {
        this.id = fleetRental.getId();
        this.carList = fleetRental.toCarDTO();
        this.customer = new CorporateCustomerDTO(fleetRental.getCustomer());
        this.returnDate = fleetRental.getReturnDate();
        this.status = fleetRental.getStatus();
        this.totalPrice = fleetRental.getTotalPrice();
        this.agreement = fleetRental.getAgreement();
    }
}
