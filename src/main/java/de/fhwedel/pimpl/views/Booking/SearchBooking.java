package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;

import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Route(Routes.BOOKINGS_SEARCH)
@SpringComponent
@UIScope
public class SearchBooking extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final TextField bookingQuery = new TextField();
    private final Button bookingSearch = new Button("Suchen", event -> search(Optional.of(bookingQuery.getValue())));
    private final Grid<Booking> bookings = new Grid<>();

    private final PageLayout pageLayout = new PageLayout("Buchung suchen", "Suche nach einer Buchung.", bookingQuery, bookingSearch, bookings);
    private final BookingRepo bookingRepo;

    public SearchBooking(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
        this.initBookingsGrid();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.bookings.setItems();
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initBookingsGrid() {
        bookings.addColumn(Booking::getId).setHeader("ID").setSortable(true);
        bookings.addColumn(Booking::getBookingNumber).setHeader("Nummer").setSortable(true);
        bookings.addColumn(Booking::getBookingDate).setHeader("Datum").setSortable(true);
        bookings.addColumn(Booking::getBookingState).setHeader("Status").setSortable(true);
        bookings.addColumn(Booking::getComment).setHeader("Kommentar").setSortable(true);
        bookings.addColumn(Booking::getCheckInShould).setHeader("Check-In Should").setSortable(true);
        bookings.addColumn(Booking::getCheckInIs).setHeader("Check-In Is").setSortable(true);
        bookings.addColumn(Booking::getCheckOutShould).setHeader("Check-Out Should").setSortable(true);
        bookings.addColumn(Booking::getCheckOutIs).setHeader("Check-Out Is").setSortable(true);
        bookings.addColumn(Booking::getRoomPrice).setHeader("Zimmer Preis").setSortable(true);
        bookings.addColumn(Booking::getLicensePlate).setHeader("KFZ").setSortable(true);
        bookings.setSelectionMode(Grid.SelectionMode.SINGLE);
        bookings.setHeight("300px");
        bookings.setWidth("1700px");
        bookings.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                Booking booking = event.getFirstSelectedItem().get();
                this.globalState.setCurrentBookingID(booking.getId());
                Routes.navigateTo(Routes.BOOKING_EDIT);
            }
        });
    }

    public void search(Optional<String> query) {
        List<Booking> items = query.map(str -> bookingRepo.findAll()).orElse(Collections.emptyList());
        bookings.setItems(DataProvider.ofCollection(items));
    }

}
