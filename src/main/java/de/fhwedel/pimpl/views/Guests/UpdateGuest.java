package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.repos.GuestRepo;

import java.util.Optional;

@Route(Routes.GUEST_UPDATE)
public class UpdateGuest extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final TextField guestName = new TextField();
    private final TextField guestFirstName = new TextField();
    private final DatePicker guestBirthDate = new DatePicker();
    private final FormLayout guestForm = new FormLayout(guestFirstName, guestName, guestBirthDate);
    private final Button updateGuest = new Button("Gast aktualisieren", event -> updateGuest());

    private final Button deleteGuest = new Button("Gast löschen", event -> deleteGuest());
    private final Button backButton = new Button("Zurück", event -> Routes.navigateTo(Routes.BOOKING_CHECK_IN));
    private final HorizontalLayout navigation = new HorizontalLayout(backButton, updateGuest, deleteGuest);
    private final PageLayout pageLayout = new PageLayout("Gast aktualisieren", "Aktualisiere die Daten des Gastes", guestForm, navigation);

    private Guest currentGuest = null;
    private Booking currentBooking = null;
    private final BookingRepo repo;
    private final GuestRepo guestRepo;

    public UpdateGuest(BookingRepo repo, GuestRepo guestRepo) {
        this.repo = repo;
        this.guestRepo = guestRepo;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (this.globalState.getCurrentGuestID() == -1 || this.globalState.getCurrentBookingID() == -1) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        Optional<Guest> guest = this.guestRepo.findById(this.globalState.getCurrentGuestID());
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (guest.isEmpty() || booking.isEmpty()) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        this.currentGuest = guest.get();
        this.currentBooking = booking.get();

        this.guestName.setValue(this.currentGuest.getName());
        this.guestFirstName.setValue(this.currentGuest.getFirstName());
        this.guestBirthDate.setValue(this.currentGuest.getBirthDate());
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void updateGuest() {
        this.currentGuest.setName(this.guestName.getValue());
        this.currentGuest.setFirstName(this.guestFirstName.getValue());
        this.currentGuest.setBirthDate(this.guestBirthDate.getValue());

        this.currentBooking.updateGuest(this.currentGuest);

        this.repo.save(this.currentBooking);
        Routes.navigateTo(Routes.BOOKING_CHECK_IN);
    }

    private void deleteGuest() {
        this.currentBooking.removeGuest(this.currentGuest);
        this.globalState.setCurrentGuestID(-1);
        this.repo.save(this.currentBooking);
        Routes.navigateTo(Routes.BOOKING_CHECK_IN);
    }
}
