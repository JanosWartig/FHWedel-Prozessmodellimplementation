package de.fhwedel.pimpl.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Guest {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "guest_id")
    private Integer id;

    private String name;

    private String firstName;

    private LocalDate birthDate;

    // Optional
    private LocalDate checkIn;

    // Optional
    private LocalDate checkOut;

}
