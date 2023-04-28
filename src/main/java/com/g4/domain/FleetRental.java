package com.g4.domain;

import com.g4.enums.RentalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fleet_rental")
public class FleetRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull(message = "Car must be provided for rental process")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "fleet_car_rental",
            joinColumns = {@JoinColumn(name = "f_rent_id")},
            inverseJoinColumns = {@JoinColumn(name = "f_car_id")}
    )
    private List<Car> carList = new ArrayList<>();


    @NotNull(message = "Customer must be provided for rental process")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CorporateCustomer customer;

    @Setter(AccessLevel.NONE)
    private final LocalDate rentalDate = LocalDate.now();

    @NotNull(message = "Return date must be provided")
    @Column(nullable = false)
    private LocalDate returnDate;

    @NotNull(message = "Status should be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @Lob
    private byte[] agreement;
}