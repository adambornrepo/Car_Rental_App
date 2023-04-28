package com.g4.domain.abstracts;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(length = 50, nullable = false)
    private String name;

    @NotBlank(message = "Lastname cannot be empty")
    @Column(length = 30, nullable = false)
    private String lastname;

    @NotBlank(message = "Phone number cannot be empty")
    @Column(length = 30, unique = true)
    private String phoneNumber;

    @NotBlank(message = "Username cannot be white space or empty")
    @Column(length = 50, nullable = false, unique = true)//unique var
    private String username;

    @NotBlank(message = "Password cannot be white space or empty")
    @Column(nullable = false)
    private String password;

}
