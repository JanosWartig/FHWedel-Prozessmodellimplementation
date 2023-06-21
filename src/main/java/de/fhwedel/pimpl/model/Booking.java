package de.fhwedel.pimpl.model;

import jakarta.persistence.*;

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

    private Integer bookingNumber;

    private LocalDate bookingDate;

    private BookingState bookingState;

    private String comment;

    private LocalDate CheckInShould;

    private LocalDate CheckInIs;

    private LocalDate CheckOutShould;

    private LocalDate CheckOutIs;

    private Integer roomPrice;

    private String licensePlate;


}
