package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;
import de.fhwedel.pimpl.repos.RoomRepo;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.ROOM_AVAILABLE)
@SpringComponent
@UIScope
public class AvailableRooms extends VerticalLayout implements BeforeEnterObserver {

    private HeadlineSubheadlineView headlineSubheadlineView = new HeadlineSubheadlineView(
            "Verfügbare Zimmer ermitteln",
            "Wähle ein Zimmer aus.",
            Constants.HEADLINE_1);

    private ForwardBackwardNavigationView forwardBackwardNavigationView = new ForwardBackwardNavigationView(
            "Kein passendes Zimmer gefunden", "Zimmerkategorie bearbeiten"
    );

    private Button calculatePrice = new Button("Preis bestimmen");

    private Grid<Room> rooms = new Grid<>();

    private VerticalLayout view = new VerticalLayout(headlineSubheadlineView, rooms, forwardBackwardNavigationView, calculatePrice);

    private RoomCategoryRepo roomCategoryRepo;
    private RoomRepo roomRepo;

    public AvailableRooms(RoomCategoryRepo roomCategoryRepo, RoomRepo roomRepo) {
        this.roomCategoryRepo = roomCategoryRepo;
        this.roomRepo = roomRepo;

        this.forwardBackwardNavigationView.getBack().addClickListener(event -> Routes.navigateTo(Routes.ROOM_START));

        rooms.addColumn(Room::getRoomNumber).setHeader("Zimmernummer").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getName()).setHeader("Zimmerkategorie").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getNumberOfBeds()).setHeader("Bettanzahl").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getPrice()).setHeader("Preis").setSortable(true);
        rooms.addColumn(room -> room.getRoomCategory().getMinPrice()).setHeader("Min Preis").setSortable(true);
        rooms.setSelectionMode(Grid.SelectionMode.SINGLE);
        rooms.setHeight("300px");
        rooms.addSelectionListener(item -> {
            if(item.getFirstSelectedItem().isPresent()) {
                Room room = item.getFirstSelectedItem().get();
                this.forwardBackwardNavigationView.getNext().setVisible(false);
                this.forwardBackwardNavigationView.getBack().setVisible(false);
                this.calculatePrice.setVisible(true);

                this.calculatePrice.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
                this.calculatePrice.setIconAfterText(true);

                calculatePrice.addClickListener(event -> {
                    Routes.navigateTo(Routes.ROOM_CALCULATE_PRICE);
                });
            } else {
                this.forwardBackwardNavigationView.getBack().setVisible(true);
                this.forwardBackwardNavigationView.getNext().setVisible(true);
                this.calculatePrice.setVisible(false);
                this.forwardBackwardNavigationView.getNext().getStyle().set("color", "red");
            }
        });

        this.calculatePrice.setVisible(false);

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
            } else {
                // Es wurden keine Zimmer gefunden
                Routes.navigateTo(Routes.ROOM_NOT_FOUND);
            }

        }
    }


}
