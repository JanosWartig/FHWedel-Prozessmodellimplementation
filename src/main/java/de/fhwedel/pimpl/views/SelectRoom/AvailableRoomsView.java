package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.ConvertManager;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.BackButton;
import de.fhwedel.pimpl.components.navigation.ErrorButton;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.repos.RoomRepo;

import java.util.List;
import java.util.Objects;

@Route(Routes.SELECT_ROOM_CHECK_AVAILABLE)
@SpringComponent
@UIScope
public class AvailableRoomsView extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();

    private final BackButton backButton = new BackButton("Zimmerkategorie bearbeiten", event -> Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD));

    private final ErrorButton errorButton = new ErrorButton("Keine Zimmer verfügbar", event -> Routes.navigateTo(Routes.SELECT_ROOM_BOOKING_FAILED));

    private final ForwardButton forwardButton = new ForwardButton("Preis bestimmen", event -> {
        globalState.setCurrentRoomID(this.selectedRoom.getId());
        Routes.navigateTo(Routes.SELECT_ROOM_CALCULATE_PRICE);
    });

    private final HorizontalLayout navigationLayout = new HorizontalLayout(backButton, errorButton, forwardButton);

    private final Grid<Room> rooms = new Grid<>();

    private final PageLayout pageLayout = new PageLayout("Verfügbare Zimmer ermitteln", "Wähle ein Zimmer aus.", rooms, navigationLayout);

    private Room selectedRoom = null;
    private final RoomRepo roomRepo;

    private final BookingRepo bookingRepo;

    public AvailableRoomsView(RoomRepo roomRepo, BookingRepo bookingRepo) {
        this.roomRepo = roomRepo;
        this.bookingRepo = bookingRepo;
        this.initRoomsTable();
        this.forwardButton.setEnabled(false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<Room> rooms = roomRepo.findByRoomCategoryId(GlobalState.getInstance().getCurrentRoomCategoryID());
        List<Booking> bookings = this.bookingRepo.findAll();
        System.out.println("Task: Verfügbare Zimmer ermitteln");
        System.out.println("Check-In: " + globalState.getBookingCheckIn());
        System.out.println("Check-Out: " + globalState.getBookingCheckOut());
        List<Booking> allReservedOrCheckedInBookings = bookings
                .stream()
                .filter(booking -> Objects.equals(booking.getBookingState().toString(), Booking.BookingState.Reserved.toString())
                        || Objects.equals(booking.getBookingState().toString(), Booking.BookingState.CheckedIn.toString())).toList();
        System.out.println("Alle Reservierten oder eingecheckten Buchungen: " + allReservedOrCheckedInBookings);
        // Überprüfen, ob Zimmer unbelegt ist, wenn es belegt ist dann rausfiltern
        allReservedOrCheckedInBookings.forEach(booking -> {
            // Die Ist Daten von einer Buchung den Soll Daten bevorzugen
            if (booking.getCheckOutIs() != null) {
                if ((!booking.getCheckOutIs().isBefore(globalState.getBookingCheckIn())
                        || !booking.getCheckOutIs().isEqual(globalState.getBookingCheckIn()))) {
                    rooms.removeIf(room -> Objects.equals(room.getId(), booking.getRoom().getId()));
                    return;
                }
            }

            if (booking.getCheckInIs() != null) {
                if (((!booking.getCheckInIs().isAfter(globalState.getBookingCheckOut())
                        || !booking.getCheckInIs().isEqual(globalState.getBookingCheckOut())))) {
                    rooms.removeIf(room -> Objects.equals(room.getId(), booking.getRoom().getId()));
                    return;
                }
            }

            // Die Soll Daten falls es keine Ist Daten gibt
            if ((!booking.getCheckOutShould().isBefore(globalState.getBookingCheckIn())
                    || !booking.getCheckOutShould().isEqual(globalState.getBookingCheckIn()))) {
                rooms.removeIf(room -> Objects.equals(room.getId(), booking.getRoom().getId()));
                System.out.println("Bedingung 1");
                return;
            }

            if (((!booking.getCheckInShould().isAfter(globalState.getBookingCheckOut())
                    || !booking.getCheckInShould().isEqual(globalState.getBookingCheckOut())))) {
                System.out.println("Bedingung 2");
                rooms.removeIf(room -> Objects.equals(room.getId(), booking.getRoom().getId()));
            }
        });

        this.rooms.setItems(DataProvider.ofCollection(rooms));
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initRoomsTable() {
        rooms.addColumn(Room::getRoomNumber).setHeader("Zimmernummer").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getName()).setHeader("Zimmerkategorie").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getNumberOfBeds()).setHeader("Bettanzahl").setSortable(true);
        rooms.addColumn(room -> ConvertManager.convertCentToEuro(room.getRoomCategory().getPrice()) + "€").setHeader("Preis").setSortable(true);
        rooms.addColumn(room -> ConvertManager.convertCentToEuro(room.getRoomCategory().getMinPrice()) + "€").setHeader("Min Preis").setSortable(true);
        rooms.setSelectionMode(Grid.SelectionMode.SINGLE);
        rooms.setHeight("200px");
        rooms.setWidth("700px");
        rooms.addSelectionListener(item -> {
            if (item.getFirstSelectedItem().isPresent()) {
                this.forwardButton.setEnabled(true);
                this.selectedRoom = item.getFirstSelectedItem().get();
            } else {
                this.forwardButton.setEnabled(false);
            }
        });
    }

}
