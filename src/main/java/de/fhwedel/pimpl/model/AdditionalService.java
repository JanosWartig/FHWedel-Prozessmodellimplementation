package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class AdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "add_service_id")
    private Integer id;

    @NotNull(message = "Pflichtangabe")
    private String name;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Der Preis darf nicht geringer als 0 sein.")
    private Integer price;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Die Umsatzsteuer darf nicht geringer als 0 sein,")
    private Integer valueAddedTaxPercent = 19;

    public AdditionalService() {}

    // Wenn der Wert der Umsatzsteuer nicht angegeben wird, wird der Standardwert von 19% verwendet.
    public AdditionalService(String name, Integer price, Integer valueAddedTaxPercent) {
        this.name = name;
        this.price = price;
        if (valueAddedTaxPercent != null) this.valueAddedTaxPercent = valueAddedTaxPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getValueAddedTaxPercent() {
        return valueAddedTaxPercent;
    }

    public void setValueAddedTaxPercent(Integer valueAddedTaxPercent) {
        this.valueAddedTaxPercent = valueAddedTaxPercent;
    }

}
