package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "claim_id")
    private Integer id;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Der Wert muss mindestens 0 sein")
    private Integer amount = 0;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Der Wert muss mindestens 0 sein")
    private Integer price;

    @NotNull(message = "Pflichtangabe")
    @Min(value = 0, message = "Der Wert muss mindestens 0 sein")
    private Integer valueAddedTax;

    @NotNull(message = "Pflichtangabe")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "add_service_id")
    private AdditionalService additionalService;

    public Claim() { }
    public Claim(Integer amount, Integer price, Integer valueAddedTax, LocalDate date) {
        this.amount = amount;
        this.price = price;
        this.valueAddedTax = valueAddedTax;
        this.date = date;
    }


    public Integer getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getValueAddedTax() {
        return valueAddedTax;
    }

    public void setValueAddedTax(Integer valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAdditionalService(AdditionalService additionalService) {
        this.additionalService = additionalService;
    }

    public AdditionalService getAdditionalService() {
        return additionalService;
    }

    public void setParentBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "id=" + id +
                ", amount=" + amount +
                ", price=" + price +
                ", valueAddedTax=" + valueAddedTax +
                ", date=" + date +
                ", booking=" + booking +
                ", additionalService=" + additionalService +
                '}';
    }
}
