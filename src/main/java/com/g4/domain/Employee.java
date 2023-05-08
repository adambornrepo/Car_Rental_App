package com.g4.domain;

import com.g4.enums.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "Employee name cannot be empty")
    @Column(length = 50, nullable = false)
    private String name;

    @NotBlank(message = "Employee lastname cannot be empty")
    @Column(length = 30, nullable = false)
    private String lastname;

    @NotNull(message = "Birthdate cannot be null")
    @Column(nullable = false)
    private LocalDate birthdate;


    @NotBlank(message = "Phone number cannot be empty")
    @Column(length = 30, unique = true)
    private String phoneNumber;

    @Embedded
    private Address address;


    @NotBlank(message = "Username cannot be white space or empty")
    @Column(length = 50, nullable = false, unique = true)//unique var
    private String username;

    @NotBlank(message = "Password cannot be white space or empty")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Position should be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

}
