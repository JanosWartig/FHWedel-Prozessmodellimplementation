package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Booking {

    public enum BookingState {
        Reserved,
        CheckedIn,
        CheckedOut,

        Finished,
        Canceled,

        Canceled_Finished
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    private String bookingNumber;

    @NotNull(message = "Pflichtangabe")
    private LocalDate bookingDate;

    @NotNull(message = "Pflichtangabe")
    @Enumerated(EnumType.STRING)
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

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guest> guests;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    public Booking() { }

    public Booking(LocalDate bookingDate, BookingState bookingState, String comment, LocalDate checkInShould, LocalDate checkInIs, LocalDate checkOutShould, LocalDate checkOutIs, Integer roomPrice, String licensePlate, Customer customer, Room room) {
        this.bookingDate = bookingDate;
        this.bookingState = bookingState;
        this.comment = comment;
        this.checkInShould = checkInShould;
        this.checkInIs = checkInIs;
        this.checkOutShould = checkOutShould;
        this.checkOutIs = checkOutIs;
        this.roomPrice = roomPrice;
        this.licensePlate = licensePlate;
        this.customer = customer;
        this.room = room;
        this.guests = new ArrayList<>();
        this.claims = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void addGuest(Guest guest) {
        guest.setParentBooking(this);
        this.guests.add(guest);
    }

    public void updateGuest(Guest guest) {
        for (Guest g : guests) {
            if (Objects.equals(g.getId(), guest.getId())) {
                g.setFirstName(guest.getFirstName());
                g.setName(guest.getName());
                g.setBirthDate(guest.getBirthDate());
                g.setCheckIn(guest.getCheckIn());
                g.setCheckOut(guest.getCheckOut());
            }
        }
    }

    public void removeGuest(Guest guest) {
        this.guests.removeIf(g -> Objects.equals(g.getId(), guest.getId()));
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void addClaim(Claim claim) {
        claim.setParentBooking(this);
        this.claims.add(claim);
    }


    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", bookingDate=" + bookingDate +
                ", bookingState=" + bookingState +
                ", comment='" + comment + '\'' +
                ", checkInShould=" + checkInShould +
                ", checkInIs=" + checkInIs +
                ", checkOutShould=" + checkOutShould +
                ", checkOutIs=" + checkOutIs +
                ", roomPrice=" + roomPrice +
                ", licensePlate='" + licensePlate + '\'' +
                ", customer=" + customer +
                ", room=" + room +
                ", guests=" + guests +
                ", claims=" + claims +
                '}';
    }
}
