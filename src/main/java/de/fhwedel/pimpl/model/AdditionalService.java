package de.fhwedel.pimpl.model;

import jakarta.persistence.*;

@Entity
public class AdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "add_service_id")
    private Integer id;

    private String name;

    private Integer price;

    private Integer valueAddedTaxPercent;

}
