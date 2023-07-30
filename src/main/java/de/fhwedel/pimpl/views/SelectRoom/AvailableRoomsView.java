package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomRepo;

import java.util.List;

@SuppressWarnings("serial")
@Route(Routes.SELECT_ROOM_CHECK_AVAILABLE)
@SpringComponent
@UIScope
public class AvailableRoomsView extends VerticalLayout implements BeforeEnterObserver {

    private final Navigation navigation = new Navigation("Zimmerkategorie bearbeiten", "Preis bestimmen", "Abbruch");

    private final Grid<Room> rooms = new Grid<>();

    private final RoomRepo roomRepo;

    public AvailableRoomsView(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;

        this.navigation.getBack().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD));
        this.navigation.getCancel().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_BOOKING_FAILED));
        this.navigation.setFinishButtonActive(false);

        rooms.addColumn(Room::getRoomNumber).setHeader("Zimmernummer").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getName()).setHeader("Zimmerkategorie").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getNumberOfBeds()).setHeader("Bettanzahl").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getPrice()).setHeader("Preis").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getMinPrice()).setHeader("Min Preis").setSortable(true);
        rooms.setSelectionMode(Grid.SelectionMode.SINGLE);
        rooms.setHeight("200px");
        rooms.setWidth("700px");
        rooms.addSelectionListener(item -> {
            if (item.getFirstSelectedItem().isPresent()) {

                this.navigation.setFinishButtonActive(true);

                this.navigation.getForwardNavigation().addClickListener(event -> {
                    GlobalState globalState = GlobalState.getInstance();
                    globalState.getCurrentBooking().setRoom(item.getFirstSelectedItem().get());
                    Routes.navigateTo(Routes.SELECT_ROOM_CALCULATE_PRICE);
                });
            } else {
                this.navigation.setFinishButtonActive(false);
            }
        });

        Header header = new Header("Verfügbare Zimmer ermitteln", "Wähle ein Zimmer aus.");
        VerticalLayout view = new VerticalLayout(header, rooms, navigation);
        this.add(view);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RoomCategory roomCategory = GlobalState.getInstance().getSelectedRoomCategory();

        List<Room> rooms = roomRepo.findByRoomCategory(roomCategory);

        if (!rooms.isEmpty()) { this.rooms.setItems(DataProvider.ofCollection(rooms)); }

    }


}
