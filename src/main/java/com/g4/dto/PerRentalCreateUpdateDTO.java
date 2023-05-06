package com.g4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.g4.enums.RentalStatus;
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
public class PerRentalCreateUpdateDTO {

    @NotNull(message = "Car plate number must be provided for rental process")
    private String carPlateNum;

    @NotNull(message = "Customer phone number must be provided for rental process")
    private String customerPhoneNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Return date must be provided")
    private LocalDate returnDate;

    @NotNull(message = "Status should be selected")
    private RentalStatus status;

}
