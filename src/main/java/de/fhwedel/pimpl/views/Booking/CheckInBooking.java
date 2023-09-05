package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.components.CustomDialog;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.views.Guests.CheckInGuests;

import java.util.Optional;

@Route(Routes.BOOKING_CHECK_IN)
@SpringComponent
@UIScope
public class CheckInBooking extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final PageLayout pageLayout = new PageLayout("Buchung einchecken", "Checke die Buchung ein.");;

    private Booking currentBooking = null;
    private final BookingRepo repo;

    public CheckInBooking(BookingRepo repo) {
        this.repo = repo;
        ForwardButton forwardButton = new ForwardButton("Buchung einchecken", event -> this.handleBookingCheckIn());
        forwardButton.setEnabled(true);
        this.pageLayout.add(new CheckInGuests(repo));
        this.pageLayout.add(forwardButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (this.globalState.getCurrentBookingID() == -1) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

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

    private void handleBookingCheckIn() {
        CustomDialog customDialog = new CustomDialog("Die Buchung wurde erfolgreich eingecheckt.", "Fortfahren.");
        customDialog.open();
        customDialog.getCloseButton().addClickListener(event -> {
            this.currentBooking.setCheckInIs(this.globalState.getCurrentDate());
            this.currentBooking.setBookingState(Booking.BookingState.CheckedIn);
            this.repo.save(this.currentBooking);
            customDialog.getDialog().close();
            Routes.navigateTo(Routes.PRE_CHECKOUT_ADDITIONAL_GUESTS);
        });
    }
}
