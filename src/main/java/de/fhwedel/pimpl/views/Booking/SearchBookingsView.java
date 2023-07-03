package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Booking;

import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.BOOKINGS_SEARCH)
@SpringComponent
@UIScope
public class SearchBookingsView extends VerticalLayout implements BeforeEnterObserver {

    private TextField bookingQuery = new TextField();
    private Button bookingSearch = new Button("Suchen", event -> search( Optional.of(bookingQuery.getValue()) ));
    private Grid<Booking> bookings = new Grid<>();

    private VerticalLayout customersForm = new VerticalLayout(
            new Header("Buchung suchen", "Suche nach einer Buchung."),
            bookingQuery, bookingSearch, bookings);
    private VerticalLayout view;
    private BookingRepo bookingRepo;

    public SearchBookingsView(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
        this.initBookingsGrid();

        customersForm.setPadding(false);

        view = new VerticalLayout(customersForm);

        add(view);
    }

    private void initBookingsGrid() {
        bookings.addColumn(Booking::getBookingNumber).setHeader("Buchungsnummer").setSortable(true);
        bookings.addColumn(Booking::getBookingDate).setHeader("Buchungsdatum").setSortable(true);
        bookings.addColumn(Booking::getBookingState).setHeader("Status").setSortable(true);
        bookings.addColumn(Booking::getComment).setHeader("Kommentar").setSortable(true);
        bookings.addColumn(Booking::getCheckInShould).setHeader("Check-In Should").setSortable(true);
        bookings.addColumn(Booking::getCheckInIs).setHeader("Check-In Is").setSortable(true);
        bookings.addColumn(Booking::getCheckOutShould).setHeader("Check-Out Should").setSortable(true);
        bookings.addColumn(Booking::getCheckOutIs).setHeader("Check-Out Is").setSortable(true);
        bookings.addColumn(Booking::getRoomPrice).setHeader("Preis").setSortable(true);
        bookings.addColumn(Booking::getLicensePlate).setHeader("KFZ-Zeichen").setSortable(true);
        bookings.setSelectionMode(Grid.SelectionMode.SINGLE);
        bookings.setHeight("300px");
        bookings.addSelectionListener(event -> {
            if(event.getFirstSelectedItem().isPresent()) {

            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.bookings.setItems();
    }

    public void search(Optional<String> query) {
        List<Booking> items = query.map(str -> bookingRepo.findAll()).orElse(Collections.emptyList());
        bookings.setItems(DataProvider.ofCollection(items));
    }


}
