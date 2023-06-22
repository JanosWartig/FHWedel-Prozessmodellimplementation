package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "guest_id")
    private Integer id;

    @NotNull(message = "Pflichtangabe")
    private String name;

    @NotNull(message = "Pflichtangabe")
    private String firstName;

    @NotNull(message = "Pflichtangabe")
    private LocalDate birthDate;

    // Optional
    private LocalDate checkIn;

    // Optional
    private LocalDate checkOut;

    public Guest() { }

    public Guest(String name, String firstName, LocalDate birthDate, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("Check-In-Datum muss kleiner als Check-Out-Datum sein.");
        }
        this.name = name;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
