package com.g4.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerStatus {

    AVAILABLE("Available"),
    HAS_RENTED("HasRented"),
    SUSPENDED("Suspended"),
    TERMINATED("Terminated");

    private final String value;

    public static CustomerStatus getDefault() {
        return AVAILABLE;
    }
}
