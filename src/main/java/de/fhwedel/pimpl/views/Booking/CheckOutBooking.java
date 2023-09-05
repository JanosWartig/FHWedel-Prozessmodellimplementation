package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
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
    private final VerticalLayout claimsLayout = new VerticalLayout(headlineClaims, claims);

    private final ForwardButton forwardButton = new ForwardButton("Buchung auschecken", event -> this.checkOutBooking());
    private final PageLayout pageLayout = new PageLayout("Buchung auschecken",
            "Die Buchung des Kunden auschecken.", checkOutGuestsLayout, claimsLayout, this.forwardButton);

    private Guest selectedGuest;
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
        this.claims.addColumn(Claim::getPrice).setHeader("Beschreibung").setSortable(true);
        this.claims.addColumn(Claim::getAmount).setHeader("Preis").setSortable(true);
        this.claims.addColumn(Claim::getValueAddedTax).setHeader("USt").setSortable(true);
    }

    private void checkOutGuest() {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) return;

        this.selectedGuest.setCheckOut(this.globalState.getCurrentDate());
        booking.get().updateGuest(this.selectedGuest);
        Booking newBooking = this.repo.save(booking.get());
        this.guests.setItems(newBooking.getGuests());
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
