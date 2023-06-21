package de.fhwedel.pimpl.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Claims {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "claims_id")
    private Integer id;

    private Integer amount;

    private Integer price;

    private Integer valueAddedTax;

    private LocalDate date;


}
