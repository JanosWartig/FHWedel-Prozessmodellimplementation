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
    @Column(name = "roomCategory_id")
    private Integer id;

    private String name;

    @Size(min = 1, message = "Mindestens ein Bett pro Zimmer.")
    private Integer numberOfBeds;

    @Size(min = 100, message = "Dies ist der Standardpreis.")
    private Integer price;

    @Size(min = 50, message = "Dies ist der minimale Preis.")
    private Integer minPrice;

    public RoomCategory() {}

    public RoomCategory(String name, Integer numberOfBeds, Integer price, Integer minPrice) {
        this();
        this.name = name;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
        this.minPrice = minPrice;
    }

    public String getName() {
        return this.name;
    }

    public Integer getNumberOfBeds() {
        return this.numberOfBeds;
    }

    public int getPrice() {
        return this.price;
    }

    public int getMinPrice() {
        return this.minPrice;
    }

}
