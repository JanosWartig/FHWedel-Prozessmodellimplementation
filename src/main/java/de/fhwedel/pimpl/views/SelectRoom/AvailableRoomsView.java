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
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.BackButton;
import de.fhwedel.pimpl.components.navigation.ErrorButton;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.repos.RoomRepo;

import java.util.List;

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

    public AvailableRoomsView(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
        this.initRoomsTable();
        this.forwardButton.setEnabled(false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<Room> rooms = roomRepo.findByRoomCategoryId(GlobalState.getInstance().getCurrentRoomCategoryID());
        if (!rooms.isEmpty()) this.rooms.setItems(DataProvider.ofCollection(rooms));
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initRoomsTable() {
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
                this.forwardButton.setEnabled(true);
                this.selectedRoom = item.getFirstSelectedItem().get();
            } else {
                this.forwardButton.setEnabled(false);
            }
        });
    }

}
