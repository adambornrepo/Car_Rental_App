package com.g4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FleetRentalCreateDTO {

    @NotNull(message = "Car must be provided for rental process")
    private List<String> carPlateNumList = new ArrayList<>();

    @NotNull(message = "Customer must be provided for rental process")
    private String customerPhoneNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Return date must be provided")
    private LocalDate returnDate;

    @NotNull(message = "Status should be selected")
    private RentalStatus status;

    @Positive(message = "Total price cannot be negative")
    @NotNull(message = "Total price cannot be null")
    private Double totalPrice;

    private byte[] agreement;


}
