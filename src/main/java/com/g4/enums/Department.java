package com.g4.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Department {

    FLEET("Fleet"),
    PERSONAL("Personal");

    private final String values;
}
