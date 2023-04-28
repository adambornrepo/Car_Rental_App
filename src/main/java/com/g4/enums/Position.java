package com.g4.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    MANAGER("Manager"),
    SALES_AGENT("Sales Agent"),
    INACTIVE("Inactive");

    private final String value;
}
