package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.*;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

@SuppressWarnings("serial")
@Route(Routes.GUEST_ADD_GUEST)
@SpringComponent
@UIScope
public class AddGuests extends VerticalLayout implements BeforeEnterObserver {

    private final Navigation navigation = new Navigation("Alle Gäste erfasst und Buchungsbestätigung versenden", false);

    private final Header header = new Header("Gäste hinzufügen", "Füge der aktuellen Buchung Gäste hinzu.");

    private final Button addGuest = new Button("Gast hinzufügen");

    private final TextField guestName = new TextField();
    private final TextField guestFirstName = new TextField();
    private final DatePicker guestBirthDate = new DatePicker();

    private final FormLayout guestForm = new FormLayout();

    private final VerticalLayout view = new VerticalLayout(header, guestForm, addGuest, navigation);

    private final BookingRepo repo;

    public AddGuests(BookingRepo repo) {
        this.repo = repo;
        this.configureLayout();

        this.navigation.getForwardNavigation().addClickListener(event -> {
            CustomDialog customDialog = new CustomDialog("Buchungsbestätigung wurde versendet.",  "Zur Kenntnis genommen");
            customDialog.open();
            customDialog.getCloseButton().addClickListener(closeEvent -> {
                customDialog.getDialog().close();
                Routes.navigateTo(Routes.EVENTS_AFTER_BOOKING_COMPLETED);
            });
        });

        this.view.setWidth("900px");
        this.add(view);

        this.addGuestClickEvent();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (GlobalState.getInstance().getCurrentBooking() == null) Routes.navigateTo(Routes.SEARCH_CUSTOMER);
    }

    private void addGuestClickEvent() {
        this.addGuest.addClickListener(event -> {

            if (this.guestName.getValue().isEmpty() || this.guestFirstName.getValue().isEmpty() || this.guestBirthDate.getValue() == null) {
                Notifications.showErrorNotification("Die Felder Gast Name, Gast Vorname und Geburtsdatum müssen ausgefüllt sein.");
                return;
            }

            Guest newGuest = new Guest(this.guestName.getValue(), this.guestFirstName.getValue(), this.guestBirthDate.getValue());

            GlobalState globalState = GlobalState.getInstance();

            int numberOfBeds = globalState.getCurrentBooking().getRoom().getRoomCategory().getNumberOfBeds();
            int currentGuests = globalState.getCurrentBooking().getGuests().size();
            boolean isSupervisor = globalState.isSupervisorModeActive();

            if (currentGuests >= numberOfBeds && !isSupervisor) {
                Notifications.showErrorNotification("Die maximale Anzahl an Gästen für dieses Zimmer ist erreicht.");
                return;
            }

            globalState.getCurrentBooking().addGuest(newGuest);

            this.repo.save(globalState.getCurrentBooking());
        });
    }

    private void configureLayout() {
        this.navigation.getForwardNavigation().setEnabled(true);
        this.guestForm.addFormItem(guestName, "Gast Name");
        this.guestForm.addFormItem(guestFirstName, "Gast Vorname");
        this.guestForm.addFormItem(guestBirthDate, "Geburtsdatum");
        this.addGuest.setIcon(VaadinIcon.PLUS.create());
    }

}
