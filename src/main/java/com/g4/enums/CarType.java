package com.g4.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarType {

    SEDAN("Sedan"),
    HATCHBACK("Hatchback"),
    SUV("Suv"),
    TRUCK("Truck"),
    VAN("Van");

    private final String values;
}
