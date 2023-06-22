package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Booking {

    private enum BookingState {
        Reserved,
        CheckedIn,
        CheckedOut,
        Finished,
        Canceled,
        Canceled_Finished
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "booking_id")
    private Integer id;

    @NotNull(message = "Pflichtangabe")
    @Column(unique=true, updatable = false)
    private Integer bookingNumber;

    @NotNull(message = "Pflichtangabe")
    private LocalDate bookingDate;

    @NotNull(message = "Pflichtangabe")
    private BookingState bookingState = BookingState.Reserved;

    @NotNull(message = "Pflichtangabe")
    private String comment = "";

    @NotNull(message = "Pflichtangabe")
    private LocalDate checkInShould;

    private LocalDate checkInIs;

    @NotNull(message = "Pflichtangabe")
    private LocalDate checkOutShould;

    private LocalDate checkOutIs;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Der Wert muss mindestens 0 sein")
    private Integer roomPrice;

    private String licensePlate;

    public Booking() { }

    public Booking(Integer bookingNumber, LocalDate bookingDate, LocalDate checkInShould, LocalDate checkInIs, LocalDate checkOutShould, LocalDate checkOutIs, Integer roomPrice, String licensePlate) {
        if (checkInShould.isBefore(bookingDate) || checkInIs.isBefore(bookingDate)) {
            throw new IllegalArgumentException("Check-In-Datum muss größer oder gleich dem Buchungsdatum sein.");
        }
        if (checkInShould.isAfter(checkOutShould) || checkInIs.isAfter(checkOutIs)) {
            throw new IllegalArgumentException("Check-Out-Datum muss größer dem Buchungsdatum sein.");
        }

        this.bookingNumber = bookingNumber;
        this.bookingDate = bookingDate;
        this.checkInShould = checkInShould;
        this.checkInIs = checkInIs;
        this.checkOutShould = checkOutShould;
        this.checkOutIs = checkOutIs;
        this.roomPrice = roomPrice;
        this.licensePlate = licensePlate;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingState bookingState) {
        this.bookingState = bookingState;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCheckInShould() {
        return checkInShould;
    }

    public void setCheckInShould(LocalDate checkInShould) {
        this.checkInShould = checkInShould;
    }

    public LocalDate getCheckInIs() {
        return checkInIs;
    }

    public void setCheckInIs(LocalDate checkInIs) {
        this.checkInIs = checkInIs;
    }

    public LocalDate getCheckOutShould() {
        return checkOutShould;
    }

    public void setCheckOutShould(LocalDate checkOutShould) {
        this.checkOutShould = checkOutShould;
    }

    public LocalDate getCheckOutIs() {
        return checkOutIs;
    }

    public void setCheckOutIs(LocalDate checkOutIs) {
        this.checkOutIs = checkOutIs;
    }

    public Integer getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Integer roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
