package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "room_id")
    private Integer id;

    @NotNull(message = "Pflichtangabe")
    private Integer roomNumber;

    @ManyToOne
    @JoinColumn(name = "roomCategory_id")
    private RoomCategory roomCategory;

    public Room() { }

    public Room(Integer roomNumber, RoomCategory roomCategory) {
        this.roomNumber = roomNumber;
        this.roomCategory = roomCategory;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
    }

}
