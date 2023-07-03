package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.Guest;

import java.util.List;
import java.util.Optional;

public class GuestCheckInView extends VerticalLayout {

    private Label headline = new Label("Gast einchecken");
    private Label subheadLine = new Label("Überprüfe ob die Daten aller Gäste korrekt sind.");
    private VerticalLayout description = new VerticalLayout(headline, subheadLine);

    private TextField guestQuery = new TextField();
    private Button guestSearch = new Button("Suchen", event -> searchGuests( Optional.of(guestQuery.getValue()) ));
    private Grid<Guest> guests = new Grid<>();
    private final VerticalLayout view = new VerticalLayout(guestQuery, guestSearch, guests);

    public GuestCheckInView() {
        this.view.setPadding(false);
        this.configureGuestTable();
        this.headline.getStyle().set("font-weight", "bold").set("font-size", Constants.HEADLINE_1).set("margin-top", "30px");
        this.description.setPadding(false);
        this.description.setSpacing(false);

        this.add(this.description, this.view);
    }

    private void configureGuestTable() {
        this.guests.addColumn(Guest::getName).setHeader("Nachname").setSortable(true);
        this.guests.addColumn(Guest::getFirstName).setHeader("Vorname").setSortable(true);
        this.guests.addColumn(Guest::getBirthDate).setHeader("Geburtsdatum").setSortable(true);
        this.guests.addColumn(Guest::getCheckIn).setHeader("Check-In").setSortable(true);
        this.guests.addColumn(Guest::getCheckOut).setHeader("Check-Out").setSortable(true);
        this.guests.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.guests.setHeight("300px");
    }

    public void searchGuests(Optional<String> query) {
        GlobalState globalState = GlobalState.getInstance();
        Booking currentBooking = globalState.getCurrentBooking();
        List<Guest> guests = currentBooking.getGuests();
        this.guests.setItems(DataProvider.ofCollection(guests));
    }



}
