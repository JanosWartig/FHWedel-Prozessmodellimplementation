package de.fhwedel.pimpl.model;

import jakarta.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "room_id")
    private Integer id;

    private Integer roomNumber;

    public Room() {

    }

    public Room(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

}
