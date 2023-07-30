package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

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

    @jakarta.annotation.Nullable
    private LocalDate checkIn;

    @jakarta.annotation.Nullable
    private LocalDate checkOut;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public Guest() { }

    public Guest(String name, String firstName, LocalDate birthDate) {
        this.name = name;
        this.firstName = firstName;
        this.birthDate = birthDate;
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

    public Integer getId() {
        return id;
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
