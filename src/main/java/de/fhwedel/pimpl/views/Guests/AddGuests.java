package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.*;
import de.fhwedel.pimpl.components.CustomDialog;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.Optional;

@Route(Routes.GUEST_ADD_GUEST)
@SpringComponent
@UIScope
public class AddGuests extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final Button addGuest = new Button("Gast hinzufügen");
    private final TextField guestName = new TextField();
    private final TextField guestFirstName = new TextField();
    private final DatePicker guestBirthDate = new DatePicker();
    private final FormLayout guestForm = new FormLayout();

    private final NumberField bedsCount = new NumberField();
    private final NumberField currentGuestsCount = new NumberField();
    private final FormLayout controlForm = new FormLayout();
    private final ForwardButton forwardButton = new ForwardButton("Buchungsbestätigung versenden", event -> {
        CustomDialog customDialog = new CustomDialog("Buchungsbestätigung wurde versendet.", "Zur Kenntnis genommen");
        customDialog.open();
        customDialog.getCloseButton().addClickListener(closeEvent -> {
            customDialog.getDialog().close();
            Routes.navigateTo(Routes.EVENTS_AFTER_BOOKING_COMPLETED);
        });
    });
    private final PageLayout pageLayout = new PageLayout("Gäste hinzufügen", "Füge der aktuellen Buchung Gäste hinzu.", guestForm, addGuest, controlForm, forwardButton);

    private Booking currentBooking = null;
    private final BookingRepo repo;

    public AddGuests(BookingRepo repo) {
        this.repo = repo;
        this.forwardButton.setEnabled(true);
        this.addGuest.setIcon(VaadinIcon.PLUS.create());
        this.initGuestForm();
        this.addGuestClickEvent();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (globalState.getCurrentBookingID() == -1) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        Optional<Booking> booking = this.repo.findById(globalState.getCurrentBookingID());

        if (booking.isEmpty()) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        this.currentBooking = booking.get();
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initGuestForm() {

        if (globalState.getCurrentBookingID() == -1) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        Optional<Booking> booking = this.repo.findById(globalState.getCurrentBookingID());

        if (booking.isEmpty()) return;

        this.guestForm.addFormItem(guestName, "Gast Name");
        this.guestForm.addFormItem(guestFirstName, "Gast Vorname");
        this.guestForm.addFormItem(guestBirthDate, "Geburtsdatum");

        this.controlForm.addFormItem(bedsCount, "Anzahl Betten");
        this.controlForm.addFormItem(currentGuestsCount, "Aktuelle Anzahl an Gästen");
        this.bedsCount.setEnabled(false);
        this.currentGuestsCount.setEnabled(false);
        this.bedsCount.setValue(booking.get().getRoom().getRoomCategory().getNumberOfBeds().doubleValue());
        this.currentGuestsCount.setValue((double) booking.get().getGuests().size());
    }

    private void addGuestClickEvent() {
        this.addGuest.addClickListener(event -> {

            if (this.guestName.getValue().isEmpty() || this.guestFirstName.getValue().isEmpty() || this.guestBirthDate.getValue() == null) {
                Notifications.showErrorNotification("Die Felder Gast Name, Gast Vorname und Geburtsdatum müssen ausgefüllt sein.");
                return;
            }

            Guest newGuest = new Guest(this.guestName.getValue(), this.guestFirstName.getValue(), this.guestBirthDate.getValue());

            int numberOfBeds = this.currentBooking.getRoom().getRoomCategory().getNumberOfBeds();
            int currentGuests = this.currentBooking.getGuests().size();
            boolean isSupervisor = globalState.isSupervisorModeActive();

            if (currentGuests >= numberOfBeds && !isSupervisor) {
                Notifications.showErrorNotification("Die maximale Anzahl an Gästen für dieses Zimmer ist erreicht.");
                return;
            }

            this.currentBooking.addGuest(newGuest);
            this.repo.save(this.currentBooking);

            this.currentGuestsCount.setValue((double)  this.currentBooking.getGuests().size());
        });
    }

}
