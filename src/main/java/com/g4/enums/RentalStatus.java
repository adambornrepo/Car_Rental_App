package com.g4.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentalStatus {
    RESERVED("Reserved"),
    RENTED("Rented"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    EXTRA_CHARGE("ExtraCharge");

    private final String value;
}
