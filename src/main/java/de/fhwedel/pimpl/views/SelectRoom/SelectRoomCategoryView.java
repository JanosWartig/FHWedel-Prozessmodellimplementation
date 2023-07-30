package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Notifications;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.SELECT_ROOM_START)
@SpringComponent
@UIScope
public class SelectRoomCategoryView extends VerticalLayout implements BeforeEnterObserver {

    private final DatePicker checkIn = new DatePicker("Anreisedatum");
    private final DatePicker checkOut = new DatePicker("Abreisedatum");
    private final HorizontalLayout checkInOut = new HorizontalLayout(checkIn, checkOut);
    private final Label describeRoomCategory = new Label("Zimmerkategorie suchen");

    private final TextField roomCategoryQuery = new TextField();
    private final Button roomCategorySearch = new Button("Suchen", event -> search(Optional.of(roomCategoryQuery.getValue())));
    private final Grid<RoomCategory> roomCategories = new Grid<>();

    private final Header header = new Header("Zimmerkategorie und Buchungszeitraum auswählen", "Wähle deine gewünschte Zimmerkategorie und den für dich passenden Buchungszeitraum aus.");

    private final Navigation navigation = new Navigation("Verfügbare Zimmer ermitteln", false);

    private final VerticalLayout roomFind = new VerticalLayout(describeRoomCategory, roomCategoryQuery, roomCategorySearch, roomCategories);
    VerticalLayout view = new VerticalLayout(header, checkInOut, roomFind, navigation);

    private RoomCategory selectedRoomCategory = null;

    private final RoomCategoryRepo roomCategoryRepo;

    public SelectRoomCategoryView(RoomCategoryRepo roomCategoryRepo) {
        this.roomCategoryRepo = roomCategoryRepo;
        this.configureLayout();
        this.configureRoomCategories();
        this.attachListeners();

        this.add(view);
    }

    private void configureLayout() {
        // Set padding, spacing, etc.
        this.setPadding(true);
        this.setSpacing(true);
        this.roomFind.setSpacing(false);
        this.roomFind.setPadding(false);
        this.checkInOut.setSpacing(true);
        this.checkInOut.setPadding(false);
        this.roomCategoryQuery.getStyle().set("margin-bottom", "15px");
        this.roomCategories.getStyle().set("margin-top", "15px");
    }

    private void configureRoomCategories() {
        roomCategories.addColumn(RoomCategory::getName).setHeader("Zimmerkategorie").setSortable(true);
        roomCategories.addColumn(RoomCategory::getNumberOfBeds).setHeader("Betten").setSortable(true);
        roomCategories.addColumn(RoomCategory::getPrice).setHeader("Preis").setSortable(true);
        roomCategories.addColumn(RoomCategory::getMinPrice).setHeader("Min Preis").setSortable(true);
        roomCategories.setSelectionMode(Grid.SelectionMode.SINGLE);
        roomCategories.setHeight("200px");
        roomCategories.setWidth("700px");
        roomCategories.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                selectedRoomCategory = event.getFirstSelectedItem().get();
            } else {
                selectedRoomCategory = null;
            }
        });
    }

    private void attachListeners() {
        GlobalState globalState = GlobalState.getInstance();

        PropertyChangeListener listener = evt -> {
            String propertyName = evt.getPropertyName();
            Object newValue = evt.getNewValue();

            if (propertyName.equals(GlobalState.CURRENT_DATE_PROPERTY_NAME)) {
                LocalDate newCurrentDate = (LocalDate) newValue;
                // Handle current date change
                this.checkIn.setValue(newCurrentDate);
                this.checkOut.setValue(newCurrentDate.plusDays(1));
            }
        };

        globalState.addPropertyChangeListener(listener);

        this.navigation.getFinish().setEnabled(true);
        this.navigation.getFinish().addClickListener(event -> {
            if (validUserInput()) {
                globalState.setSelectedRoomCategory(this.selectedRoomCategory);
                Booking booking = new Booking(
                        globalState.getCurrentDate(),
                        this.checkIn.getValue(),
                        this.checkOut.getValue(),
                        globalState.getCurrentCustomer()
                );
                globalState.setCurrentBooking(booking);
                Routes.navigateTo(Routes.SELECT_ROOM_CHECK_AVAILABLE);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.checkIn.setValue(GlobalState.getInstance().getCurrentDate());
        this.checkOut.setValue(GlobalState.getInstance().getCurrentDate().plusDays(1));
    }

    private boolean validUserInput() {
        LocalDate checkInDate = this.checkIn.getValue();
        LocalDate checkOutDate = this.checkOut.getValue();
        LocalDate today = GlobalState.getInstance().getCurrentDate();

        if (selectedRoomCategory == null) {
            Notifications.showErrorNotification("Es wurde keine Zimmerkategorie ausgewählt.");
            return false;
        }

        // Überprüfung: checkInDate >= today
        if (checkInDate.isBefore(today)) {
            Notifications.showErrorNotification("Das Check-In Datum liegt in der Vergangenheit.");
            return false;
        }

        // Überprüfung: checkOutDate > checkInDate
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            Notifications.showErrorNotification("Das Check-Out Datum liegt vor dem Check-In Datum.");
            return false;
        }

        // Überprüfung: checkOutDate <= checkInDate + 14 Tage
        LocalDate maxCheckOutDate = checkInDate.plusDays(14);
        if (checkOutDate.isAfter(maxCheckOutDate) && !GlobalState.getInstance().isSupervisorModeActive()) {
            Notifications.showErrorNotification("Die Zeitspanne zwischen Check-In und Check-Out beträgt mehr als 14 Tage.");
            return false;
        }

        return true;
    }

    public void search(Optional<String> query) {
        List<RoomCategory> items = query.map(roomCategoryRepo::findByKeyword).orElse(Collections.emptyList());
        roomCategories.setItems(DataProvider.ofCollection(items));
    }

}
