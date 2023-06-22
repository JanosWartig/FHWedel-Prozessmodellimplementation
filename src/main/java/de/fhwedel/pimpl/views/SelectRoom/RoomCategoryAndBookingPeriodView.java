package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.ROOM_START)
@SpringComponent
@UIScope
public class RoomCategoryAndBookingPeriodView extends VerticalLayout {

    private DatePicker checkIn = new DatePicker("Anreisedatum");
    private DatePicker checkOut = new DatePicker("Abreisedatum");
    private HorizontalLayout checkInOut = new HorizontalLayout(checkIn, checkOut);
    private Label describeRoomCategory = new Label("Zimmerkategorie suchen");

    private TextField roomCategoryQuery = new TextField();
    private Button roomCategorySearch = new Button("Suchen", event -> search( Optional.of(roomCategoryQuery.getValue()) ));
    private Grid<RoomCategory> roomCategories = new Grid<>();


    private HeadlineSubheadlineView headlineSubheadlineView = new HeadlineSubheadlineView(
            "Zimmerkategorie und Buchungszeitraum auswählen",
            "Wähle deine gewünschte Zimmerkategorie und den für dich passenden Buchungszeitraum aus.",
            Constants.HEADLINE_1);

    private ForwardBackwardNavigationView forwardBackwardNavigationView = new ForwardBackwardNavigationView(
      "Verfügbare Zimmer ermitteln", false
    );

    private VerticalLayout roomFind = new VerticalLayout(describeRoomCategory, roomCategoryQuery, roomCategorySearch, roomCategories);
    VerticalLayout view = new VerticalLayout(headlineSubheadlineView, checkInOut, roomFind, forwardBackwardNavigationView);

    private RoomCategoryRepo roomCategoryRepo;

    public RoomCategoryAndBookingPeriodView(RoomCategoryRepo roomCategoryRepo) {
        this.roomCategoryRepo = roomCategoryRepo;
        this.checkIn.setValue(LocalDate.now());
        this.checkOut.setValue(LocalDate.now());
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
            if(event.getFirstSelectedItem().isPresent() && this.areCheckInCheckOutDatesValid()) {
                this.forwardBackwardNavigationView.setNext(true);
                RoomCategory roomCategory = event.getFirstSelectedItem().get();

                this.forwardBackwardNavigationView.getNext().addClickListener(event2 -> {
                    UI.getCurrent().navigate(Routes.ROOM_AVAILABLE + "?id=" + roomCategory.getId());
                });

            } else {
                this.forwardBackwardNavigationView.setNext(false);
            }
        });

        this.roomCategoryQuery.getStyle().set("margin-bottom", "15px");
        this.roomCategories.getStyle().set("margin-top", "15px");

        this.checkIn.addValueChangeListener(event -> {
           System.out.println(event.getValue());
        });

        this.add(view);
    }

    private boolean areCheckInCheckOutDatesValid() {
        LocalDate checkInDate = this.checkIn.getValue();
        LocalDate checkOutDate = this.checkOut.getValue();
        LocalDate today = LocalDate.now();

        // Überprüfung: checkInDate >= today
        if (checkInDate.isBefore(today)) {
            return false;
        }

        // Überprüfung: checkOutDate > checkInDate
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
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
