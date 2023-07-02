package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.annotation.Nullable;
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

    @Nullable
    private LocalDate checkIn;

    @Nullable
    private LocalDate checkOut;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public Guest() { }

    public Guest(String name, String firstName, LocalDate birthDate, @Nullable LocalDate checkIn, @Nullable LocalDate checkOut) {
        if (checkIn != null && checkOut != null) {
            if (checkIn.isAfter(checkOut)) {
                throw new IllegalArgumentException("Check-In-Datum muss kleiner als Check-Out-Datum sein.");
            }
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

    @Nullable
    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(@Nullable LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    @Nullable
    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(@Nullable LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setParentBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthDate=" + birthDate +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", booking=" + booking +
                '}';
    }
}
