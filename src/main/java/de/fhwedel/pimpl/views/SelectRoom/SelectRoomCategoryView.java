package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Notifications;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Route(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD)
@SpringComponent
@UIScope
public class SelectRoomCategoryView extends Composite<Component> implements BeforeEnterObserver {
    private final GlobalState globalState = GlobalState.getInstance();
    private final DatePicker checkIn = new DatePicker("Anreisedatum");
    private final DatePicker checkOut = new DatePicker("Abreisedatum");
    private final HorizontalLayout checkInOut = new HorizontalLayout(checkIn, checkOut);
    private final Label labelForCategoryQuery = new Label("Zimmerkategorie suchen");
    private final TextField roomCategoryQuery = new TextField();
    private final Button roomCategorySearch = new Button("Suchen", event -> search(Optional.of(roomCategoryQuery.getValue())));
    private final Grid<RoomCategory> roomCategories = new Grid<>();
    private final ForwardButton forwardButton = new ForwardButton("Verfügbare Zimmer ermitteln", event -> {
        if (validUserInput()) {
            globalState.setSelectedRoomCategory(this.selectedRoomCategory);
            Booking booking = new Booking(globalState.getCurrentDate(), this.checkIn.getValue(), this.checkOut.getValue(), globalState.getCurrentCustomer());
            globalState.setCurrentBooking(booking);
            Routes.navigateTo(Routes.SELECT_ROOM_CHECK_AVAILABLE);
        }
    });
    private final PageLayout pageLayout = new PageLayout("Zimmerkategorie und Buchungszeitraum auswählen",
            "Wähle eine Zimmerkategorie und einen Buchungszeitraum aus.", checkInOut,
            labelForCategoryQuery, roomCategoryQuery, roomCategorySearch, roomCategories, forwardButton);
    private RoomCategory selectedRoomCategory = null;
    private final RoomCategoryRepo roomCategoryRepo;

    public SelectRoomCategoryView(RoomCategoryRepo roomCategoryRepo) {
        this.roomCategoryRepo = roomCategoryRepo;
        this.configureRoomCategories();
        this.attachListeners();
        this.forwardButton.setEnabled(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.checkIn.setValue(GlobalState.getInstance().getCurrentDate());
        this.checkOut.setValue(GlobalState.getInstance().getCurrentDate().plusDays(1));
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
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
            if (event.getFirstSelectedItem().isPresent()) selectedRoomCategory = event.getFirstSelectedItem().get();
            else selectedRoomCategory = null;
        });
    }

    private void attachListeners() {
        PropertyChangeListener listener = evt -> {
            String propertyName = evt.getPropertyName();
            Object newValue = evt.getNewValue();
            if (!propertyName.equals(GlobalState.CURRENT_DATE_PROPERTY_NAME)) return;
            LocalDate newCurrentDate = (LocalDate) newValue;
            this.checkIn.setValue(newCurrentDate);
            this.checkOut.setValue(newCurrentDate.plusDays(1));
        };
        globalState.addPropertyChangeListener(listener);
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
