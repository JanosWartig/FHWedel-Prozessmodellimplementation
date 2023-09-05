package de.fhwedel.pimpl.views.SimulateEvents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.Optional;

@Route(Routes.EVENTS_AFTER_BOOKING_COMPLETED)
@SpringComponent
@UIScope
public class EventPossibilitiesAfterBookingCompletedView extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final Button cancelBookingEvent = new Button("Kunde beantragt die Stornierung der Buchung", event -> handelCancelBookingEvent());
    private final Button checkInPlusOneDayEvent = new Button("Die Anreise verschiebt sich um einen Tag", event -> handelCancelBookingEvent());
    private final Button guestCheckInEvent = new Button("Der Gast möchte einchecken.", event -> Routes.navigateTo(Routes.BOOKING_CHECK_IN));
    private final PageLayout pageLayout = new PageLayout("Event Simulation",
            "Hier werden die Events simuliert, die nach dem Versenden der Buchungsbestätigung auftreten können.",
            cancelBookingEvent, checkInPlusOneDayEvent, guestCheckInEvent);

    private Booking currentBooking = null;
    private final BookingRepo repo;

    public EventPossibilitiesAfterBookingCompletedView(BookingRepo repo) {
        this.repo = repo;
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

    public void handelCancelBookingEvent() {
        this.currentBooking.setBookingState(Booking.BookingState.Canceled);
        this.repo.save(this.currentBooking);
        Routes.navigateTo(Routes.BOOKING_FINISHED);
    }

}
