package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;
import de.fhwedel.pimpl.repos.RoomRepo;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.SELECT_ROOM_CHECK_AVAILABLE)
@SpringComponent
@UIScope
public class AvailableRoomsView extends VerticalLayout implements BeforeEnterObserver {

    private final Navigation navigation = new Navigation(
            "Zimmerkategorie bearbeiten", "Preis bestimmen", "Abbruch"
    );

    private final Grid<Room> rooms = new Grid<>();

    private final RoomCategoryRepo roomCategoryRepo;
    private final RoomRepo roomRepo;

    public AvailableRoomsView(RoomCategoryRepo roomCategoryRepo, RoomRepo roomRepo) {
        this.roomCategoryRepo = roomCategoryRepo;
        this.roomRepo = roomRepo;

        this.navigation.getEdit().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_START));
        this.navigation.getCancel().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_BOOKING_FAILED));
        this.navigation.setFinishButtonActive(false);

        rooms.addColumn(Room::getRoomNumber).setHeader("Zimmernummer").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getName()).setHeader("Zimmerkategorie").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getNumberOfBeds()).setHeader("Bettanzahl").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getPrice()).setHeader("Preis").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getMinPrice()).setHeader("Min Preis").setSortable(true);
        rooms.setSelectionMode(Grid.SelectionMode.SINGLE);
        rooms.setHeight("300px");
        rooms.addSelectionListener(item -> {
            if(item.getFirstSelectedItem().isPresent()) {

                this.navigation.setFinishButtonActive(true);

                this.navigation.getFinish().addClickListener(event -> {
                    Routes.navigateTo(Routes.SELECT_ROOM_CALCULATE_PRICE);
                });
            } else {
                this.navigation.setFinishButtonActive(false);
            }
        });

        Header header = new Header(
                "Verfügbare Zimmer ermitteln",
                "Wähle ein Zimmer aus.",
                Constants.HEADLINE_1);
        VerticalLayout view = new VerticalLayout(header, rooms, navigation);
        this.add(view);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Integer id = GlobalState.getInstance().getSelectedRoomCategoryID();
        Optional<RoomCategory> roomCategoryOptional = this.roomCategoryRepo.findById(id);
        if (roomCategoryOptional.isPresent()) {
            RoomCategory roomCategory = roomCategoryOptional.get();
            List<Room> rooms = roomRepo.findByRoomCategory(roomCategory);

            if (!rooms.isEmpty()) {
                this.rooms.setItems(DataProvider.ofCollection(rooms));
            }

        }
    }


}