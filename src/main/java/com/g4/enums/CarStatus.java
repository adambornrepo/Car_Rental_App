package com.g4.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    RENTED("Rented"),
    MAINTENANCE("Maintenance"),
    PASSIVE("Passive"),
    LOST("Lost"),
    SOLD("Sold");

    private final String value;

    public static CarStatus getDefault() {
        return AVAILABLE;
    }
}
