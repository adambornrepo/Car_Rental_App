package com.g4.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @Column(length = 40)
    private String street;
    @Column(length = 40)
    private String city;
    @Column(length = 40)
    private String state;
    @Column(length = 40)
    private String country;
    @Column(length = 14)
    private String zipCode;

}