package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class RoomCategory {

    public enum RoomTypes {
        Small, Middle, Big
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "room_id")
    private Integer id;

    private String name;

    @Size(min = 1, message = "Mindestens ein Bett pro Zimmer.")
    private Integer numberOfBeds;

    @Size(min = 100, message = "Dies ist der Standardpreis.")
    private int price;

    @Size(min = 50, message = "Dies ist der minimale Preis.")
    private int minPrice;

}
