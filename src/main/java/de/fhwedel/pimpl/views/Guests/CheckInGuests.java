package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import de.fhwedel.pimpl.components.CustomDialog;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.List;
import java.util.Optional;

public class CheckInGuests extends Composite<Component> {

    private final GlobalState globalState = GlobalState.getInstance();
    private final Label headline = new Label("Gäste suchen und einchecken");
    private final TextField guestQuery = new TextField();
    private final Button guestSearch = new Button("Suchen", event -> searchGuests(Optional.of(guestQuery.getValue())));
    private final Button checkInGuest = new Button("Einchecken");
    private final Button updateGuest = new Button("Aktualisieren");
    private final HorizontalLayout buttons = new HorizontalLayout(checkInGuest, updateGuest);
    private final Grid<Guest> guests = new Grid<>();

    private final VerticalLayout layout = new VerticalLayout(headline, guestQuery, guestSearch, guests, buttons);

    private Guest selectedGuest = null;
    private Booking currentBooking = null;
    private final BookingRepo repo;

    public CheckInGuests(BookingRepo repo) {
        this.repo = repo;
        this.layout.setPadding(true);
        this.headline.getStyle().set("font-weight", "bold").set("font-size", "16px").set("margin-top", "10px");
        this.initTable();
        this.initCheckInGuestButton();
        this.initUpdateGuestButton();
    }

    @Override
    protected Component initContent() {
        return this.layout;
    }

    private void searchGuests(Optional<String> query) {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        this.currentBooking = booking.get();

        List<Guest> guests = this.currentBooking.getGuests();
        if (query.isPresent()) {
            guests = guests.stream().filter(guest -> guest.getName().contains(query.get())).toList();
        }

        this.guests.setItems(DataProvider.ofCollection(guests));
    }

    private void initCheckInGuestButton() {
        this.checkInGuest.setEnabled(false);
    }

    private void initUpdateGuestButton() {
        this.updateGuest.setEnabled(false);
    }

    private void toggleButtonRow() {
        this.checkInGuest.setEnabled(!this.checkInGuest.isEnabled());
        this.updateGuest.setEnabled(!this.updateGuest.isEnabled());
    }

    private void initTable() {
        this.guests.addColumn(Guest::getName).setHeader("Nachname").setSortable(true);
        this.guests.addColumn(Guest::getFirstName).setHeader("Vorname").setSortable(true);
        this.guests.addColumn(Guest::getBirthDate).setHeader("Geburtsdatum").setSortable(true);
        this.guests.addColumn(Guest::getCheckIn).setHeader("Check-In").setSortable(true);
        this.guests.addColumn(Guest::getCheckOut).setHeader("Check-Out").setSortable(true);
        this.guests.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.guests.setHeight("300px");
        this.guests.setWidth("700px");
        this.guests.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isEmpty()) return;
            this.selectedGuest = event.getFirstSelectedItem().get();
            this.toggleButtonRow();
        });
        this.checkInGuest.addClickListener(event2 -> {
            CustomDialog customDialog = new CustomDialog("Hat der Gast hat einen gültigen Personalausweis vorgezeigt und hat der Gast seine personenbezogenen Daten bestätigt?", "Bestätigen und Gast einchecken");
            customDialog.open();
            customDialog.getCloseButton().addClickListener(event3 -> {
                this.selectedGuest.setCheckIn(globalState.getCurrentDate());
                this.currentBooking.updateGuest(this.selectedGuest);

                this.repo.save(this.currentBooking);
                this.guests.setItems(DataProvider.ofCollection(this.currentBooking.getGuests()));
                customDialog.getDialog().close();
                this.guests.deselectAll();
                this.selectedGuest = null;
            });
        });
        this.updateGuest.addClickListener(action -> {
            this.globalState.setCurrentGuestID(this.selectedGuest.getId());
            Routes.navigateTo(Routes.GUEST_UPDATE);
        });
    }


}
