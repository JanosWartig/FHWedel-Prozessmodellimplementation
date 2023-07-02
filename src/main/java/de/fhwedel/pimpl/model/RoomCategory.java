package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
public class RoomCategory {

    public enum RoomTypes {
        Small, Middle, Big
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "roomCategory_id")
    private Integer id;

    @NotNull(message = "Pflichtangabe")
    private String name;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 1, message = "Mindestens ein Bett pro Zimmer.")
    private Integer numberOfBeds = 1;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Mindestens 0Euro pro Nacht.")
    private Integer price;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Mindestens 0Euro pro Nacht.")
    private Integer minPrice;

    public RoomCategory() {}

    public RoomCategory(String name, Integer numberOfBeds, Integer price, Integer minPrice) {
        if (minPrice > price) {
            throw new IllegalArgumentException("Der Mindest Preis ist größer als der Übernachtungspreis.");
        }
        this.name = name;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
        this.minPrice = minPrice;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getPrice() {
        return price;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public static RoomCategory createExampleRoomCategory(RoomTypes types, Integer numberOfBeds, Integer price, Integer minPrice) {
        return new RoomCategory(types.toString(), numberOfBeds, price, minPrice);
    }

    @Override
    public String toString() {
        return "RoomCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfBeds=" + numberOfBeds +
                ", price=" + price +
                ", minPrice=" + minPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomCategory that = (RoomCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(numberOfBeds, that.numberOfBeds) && Objects.equals(price, that.price) && Objects.equals(minPrice, that.minPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfBeds, price, minPrice);
    }
}
