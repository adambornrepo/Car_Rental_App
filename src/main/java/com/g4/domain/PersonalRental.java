package com.g4.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g4.enums.RentalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personal_rental")
public class PersonalRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull(message = "Car must be provided for rental process")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @NotNull(message = "Customer must be provided for rental process")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private PersonalCustomer customer;

    @Setter(AccessLevel.NONE)
    private final LocalDate rentalDate = LocalDate.now();

    @NotNull(message = "Return date must be provided")
    @Column(nullable = false)
    private LocalDate returnDate;

    @NotNull(message = "Status should be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

}
