package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
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
public class RoomCategoryAndBookingPeriodView extends VerticalLayout implements BeforeEnterObserver {

    private DatePicker checkIn = new DatePicker("Anreisedatum");
    private DatePicker checkOut = new DatePicker("Abreisedatum");
    private HorizontalLayout checkInOut = new HorizontalLayout(checkIn, checkOut);
    private Label describeRoomCategory = new Label("Zimmerkategorie suchen");

    private TextField roomCategoryQuery = new TextField();
    private Button roomCategorySearch = new Button("Suchen", event -> search( Optional.of(roomCategoryQuery.getValue()) ));
    private Grid<RoomCategory> roomCategories = new Grid<>();


    private Header header = new Header(
            "Zimmerkategorie und Buchungszeitraum auswählen",
            "Wähle deine gewünschte Zimmerkategorie und den für dich passenden Buchungszeitraum aus.",
            Constants.HEADLINE_1);

    private Navigation navigation = new Navigation(
      "Verfügbare Zimmer ermitteln", false
    );

    private VerticalLayout roomFind = new VerticalLayout(describeRoomCategory, roomCategoryQuery, roomCategorySearch, roomCategories);
    VerticalLayout view = new VerticalLayout(header, checkInOut, roomFind, navigation);

    private RoomCategoryRepo roomCategoryRepo;

    public RoomCategoryAndBookingPeriodView(RoomCategoryRepo roomCategoryRepo) {
        this.roomCategoryRepo = roomCategoryRepo;
        this.setPadding(true);
        this.setSpacing(true);
        this.roomFind.setSpacing(false);
        this.roomFind.setPadding(false);
        this.checkInOut.setSpacing(true);
        this.checkInOut.setPadding(false);

        roomCategories.addColumn(RoomCategory::getName).setHeader("Zimmerkategorie").setSortable(true);
        roomCategories.addColumn(RoomCategory::getNumberOfBeds).setHeader("Betten").setSortable(true);
        roomCategories.addColumn(RoomCategory::getPrice).setHeader("Preis").setSortable(true);
        roomCategories.addColumn(RoomCategory::getMinPrice).setHeader("Min Preis").setSortable(true);
        roomCategories.setSelectionMode(Grid.SelectionMode.SINGLE);
        roomCategories.setHeight("300px");
        roomCategories.addSelectionListener( event -> {
            if(event.getFirstSelectedItem().isPresent()) {
                RoomCategory roomCategory = event.getFirstSelectedItem().get();
                GlobalState.getInstance().setSelectedRoomCategoryID(roomCategory.getId());
            }
        });

        GlobalState globalState = GlobalState.getInstance();

        PropertyChangeListener listener = evt -> {
            String propertyName = evt.getPropertyName();
            Object newValue = evt.getNewValue();

            if (propertyName.equals(GlobalState.SUPERVISOR_PROPERTY_NAME)) {
                boolean newSupervisorModeActive = (boolean) newValue;
                // Handle supervisor mode change
            } else if (propertyName.equals(GlobalState.CURRENT_DATE_PROPERTY_NAME)) {
                LocalDate newCurrentDate = (LocalDate) newValue;
                // Handle current date change
                this.checkIn.setValue(newCurrentDate);
                this.checkOut.setValue(newCurrentDate.plusDays(1));
            }
        };

        globalState.addPropertyChangeListener(listener);

        this.navigation.getFinish().setEnabled(true);
        this.navigation.getFinish().addClickListener(event -> {
           if (areCheckInCheckOutDatesValid()) {
               // Und es muss eine Zimmerkategorie ausgewählt sein.
               //Routes.navigateTo(Routes.ROOM_AVAILABLE);
           }
        });

        this.roomCategoryQuery.getStyle().set("margin-bottom", "15px");
        this.roomCategories.getStyle().set("margin-top", "15px");

        this.add(view);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.checkIn.setValue(GlobalState.getInstance().getCurrentDate());
        this.checkOut.setValue(GlobalState.getInstance().getCurrentDate().plusDays(1));
    }

    private void showNotification(String message) {
        Notification notification = new Notification(message, 3000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    private boolean areCheckInCheckOutDatesValid() {
        LocalDate checkInDate = this.checkIn.getValue();
        LocalDate checkOutDate = this.checkOut.getValue();
        LocalDate today = GlobalState.getInstance().getCurrentDate();

        // Überprüfung: checkInDate >= today
        if (checkInDate.isBefore(today)) {
            this.showNotification("Das Check-In Datum liegt in der Vergangenheit.");
            return false;
        }

        // Überprüfung: checkOutDate > checkInDate
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            this.showNotification("Das Check-Out Datum liegt vor dem Check-In Datum.");
            return false;
        }

        // Überprüfung: checkOutDate <= checkInDate + 14 Tage
        LocalDate maxCheckOutDate = checkInDate.plusDays(14);
        return !checkOutDate.isAfter(maxCheckOutDate);
    }

    public void search(Optional<String> query) {
        List<RoomCategory> items = query.map(str -> roomCategoryRepo.findByKeyword(str)).orElse(Collections.emptyList());
        roomCategories.setItems(DataProvider.ofCollection(items));
    }

}
