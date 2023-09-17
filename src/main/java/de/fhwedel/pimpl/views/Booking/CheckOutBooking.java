package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.ConvertManager;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Claim;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.Optional;

@Route(Routes.BOOKING_CHECK_OUT)
@SpringComponent
@UIScope
public class CheckOutBooking extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final Label headline = new Label("Gäste auschecken");
    private final Grid<Guest> guests = new Grid<>();

    private final Button checkOutGuest = new Button("Gast auschecken", event -> this.checkOutGuest());
    private final VerticalLayout checkOutGuestsLayout = new VerticalLayout(headline, guests, checkOutGuest);

    private final Label headlineClaims = new Label("Alle erfassten Inanspruchnahmen");
    private final Grid<Claim> claims = new Grid<>();

    private final DatePicker datePicker = new DatePicker("Datum");
    private final NumberField amount = new NumberField("Menge");
    private final NumberField price = new NumberField("Preis");
    private final NumberField valueAddedTax = new NumberField("USt");
    private final HorizontalLayout selectedClaimLayoutRow = new HorizontalLayout(datePicker, amount, price, valueAddedTax);
    private final Button deleteClaim = new Button("Inanspruchnahme löschen", event -> this.deleteClaim());
    private final Button updateClaim = new Button("Inanspruchnahme aktualisieren", event -> this.updateClaim());
    private final HorizontalLayout buttons = new HorizontalLayout(deleteClaim, updateClaim);
    private final VerticalLayout claimsLayout = new VerticalLayout(headlineClaims, claims, selectedClaimLayoutRow, buttons);


    private final ForwardButton forwardButton = new ForwardButton("Buchung auschecken", event -> this.checkOutBooking());
    private final PageLayout pageLayout = new PageLayout("Buchung auschecken",
            "Die Buchung des Kunden auschecken.", checkOutGuestsLayout, claimsLayout, this.forwardButton);

    private Guest selectedGuest;

    private Claim selectedClaim;
    private final BookingRepo repo;
    public CheckOutBooking(BookingRepo repo) {
        this.repo = repo;
        this.headline.getStyle().set("font-weight", "bold").set("font-size", "16px").set("margin-top", "10px");
        this.headlineClaims.getStyle().set("font-weight", "bold").set("font-size", "16px").set("margin-top", "10px");
        this.checkOutGuestsTable();
        this.addedServicesTable();
        this.forwardButton.setEnabled(true);
        this.checkOutGuest.setEnabled(false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.buttons.setEnabled(false);
        if (this.globalState.getCurrentBookingID() == -1) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
        }

        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isPresent()) {
            this.guests.setItems(booking.get().getGuests());
            this.claims.setItems(booking.get().getClaims());
        }

    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void checkOutGuestsTable() {
        this.guests.setHeight("250px");
        this.guests.setWidth("650px");
        this.guests.addColumn(Guest::getFirstName).setHeader("Vorname").setSortable(true);
        this.guests.addColumn(Guest::getName).setHeader("Nachname").setSortable(true);
        this.guests.addColumn(Guest::getCheckIn).setHeader("Check-In").setSortable(true);
        this.guests.addColumn(Guest::getCheckOut).setHeader("Check-Out").setSortable(true);

        this.guests.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                this.checkOutGuest.setEnabled(true);
                this.selectedGuest = event.getFirstSelectedItem().get();
            } else {
                this.checkOutGuest.setEnabled(false);
                this.selectedGuest = null;
            }
        });
    }

    private void addedServicesTable() {
        this.claims.setHeight("250px");
        this.claims.setWidth("1250px");
        this.claims.addColumn(Claim::getId).setHeader("ID").setSortable(true);
        this.claims.addColumn(Claim::getDate).setHeader("Datum").setSortable(true);
        this.claims.addColumn(Claim::getAmount).setHeader("Menge").setSortable(true);
        this.claims.addColumn(claim -> ConvertManager.convertCentToEuro(claim.getPrice()) + "€").setHeader("Preis").setSortable(true);
        this.claims.addColumn(claim -> ConvertManager.convertCentToEuro(claim.getValueAddedTax()) + "€").setHeader("USt").setSortable(true);

        this.claims.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                this.selectedClaim = event.getFirstSelectedItem().get();
                this.datePicker.setValue(this.selectedClaim.getDate());
                this.amount.setValue(this.selectedClaim.getAmount().doubleValue());
                this.price.setValue(ConvertManager.convertCentToEuro(this.selectedClaim.getPrice()));
                this.valueAddedTax.setValue(ConvertManager.convertCentToEuro(this.selectedClaim.getValueAddedTax()));

                this.datePicker.setEnabled(true);
                this.amount.setEnabled(true);
                this.price.setEnabled(true);
                this.valueAddedTax.setEnabled(true);
                this.buttons.setEnabled(true);
            } else {
                this.selectedClaim = null;
                this.datePicker.setEnabled(false);
                this.amount.setEnabled(false);
                this.price.setEnabled(false);
                this.valueAddedTax.setEnabled(false);
                this.buttons.setEnabled(false);

            }
        });

    }

    private void checkOutGuest() {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) return;

        this.selectedGuest.setCheckOut(this.globalState.getCurrentDate());
        booking.get().updateGuest(this.selectedGuest);
        Booking newBooking = this.repo.save(booking.get());
        this.guests.setItems(newBooking.getGuests());
    }

    private void deleteClaim() {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) return;

        booking.get().removeClaim(this.selectedClaim);
        Booking newBooking = this.repo.save(booking.get());
        this.claims.setItems(newBooking.getClaims());
    }

    private void updateClaim() {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) return;

        this.selectedClaim.setDate(this.datePicker.getValue());
        this.selectedClaim.setAmount(this.amount.getValue().intValue());
        this.selectedClaim.setPrice(ConvertManager.convertEuroToCent(this.price.getValue()));
        this.selectedClaim.setValueAddedTax(ConvertManager.convertEuroToCent(this.valueAddedTax.getValue()));

        booking.get().updateClaim(this.selectedClaim);
        Booking newBooking = this.repo.save(booking.get());
        this.claims.setItems(newBooking.getClaims());
    }

    private void checkOutBooking() {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) return;

        booking.get().setCheckOutIs(this.globalState.getCurrentDate());
        booking.get().setBookingState(Booking.BookingState.CheckedOut);
        this.repo.save(booking.get());
        Routes.navigateTo(Routes.BOOKING_FINISHED);
    }

}
