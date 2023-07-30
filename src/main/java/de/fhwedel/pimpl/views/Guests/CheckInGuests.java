package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import de.fhwedel.pimpl.Utility.CustomDialog;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.List;
import java.util.Optional;

public class CheckInGuests extends VerticalLayout {
    private final TextField guestQuery = new TextField();
    private final Button checkInGuest = new Button("Einchecken");
    private final Button updateGuest = new Button("Aktualisieren");
    private final Grid<Guest> guests = new Grid<>();
    private final BookingRepo repo;
    public CheckInGuests(BookingRepo repo) {
        this.repo = repo;

        this.initTable();
        this.initCheckInGuestButton();
        this.initUpdateGuestButton();

        Button guestSearch = new Button("Suchen", event -> searchGuests(Optional.of(guestQuery.getValue())));
        HorizontalLayout buttons = new HorizontalLayout(checkInGuest, updateGuest);

        this.setPadding(false);
        this.add(guestQuery, guestSearch, guests, buttons);
    }
    private void searchGuests(Optional<String> query) {
        GlobalState globalState = GlobalState.getInstance();
        Booking currentBooking = globalState.getCurrentBooking();
        List<Guest> guests = currentBooking.getGuests();
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
            this.toggleButtonRow();
            if (event.getFirstSelectedItem().isEmpty()) return;
            this.checkInGuest.addClickListener(event2 -> {
                CustomDialog customDialog = new CustomDialog("Hat der Gast hat einen gültigen Personalausweis vorgezeigt und hat der Gast seine personenbezogenen Daten bestätigt?", "Bestätigen und Gast einchecken");
                customDialog.open();
                customDialog.getCloseButton().addClickListener(event3 -> {
                    GlobalState globalState = GlobalState.getInstance();
                    Guest selectedGuest = event.getFirstSelectedItem().get();
                    selectedGuest.setCheckIn(globalState.getCurrentDate());
                    globalState.getCurrentBooking().updateGuest(selectedGuest);
                    this.repo.save(globalState.getCurrentBooking());
                    this.guests.setItems(DataProvider.ofCollection(globalState.getCurrentBooking().getGuests()));
                    customDialog.getDialog().close();
                });
            });
            this.updateGuest.addClickListener(action -> {
                GlobalState globalState = GlobalState.getInstance();
                globalState.setCurrentGuest(event.getFirstSelectedItem().get());
                Routes.navigateTo(Routes.GUEST_UPDATE);
            });
        });
    }
}
