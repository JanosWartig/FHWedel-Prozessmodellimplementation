package de.fhwedel.pimpl.views.SimulateEvents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.repos.BookingRepo;

@SuppressWarnings("serial")
@Route(Routes.EVENTS_AFTER_BOOKING_COMPLETED)
@SpringComponent
@UIScope
public class EventPossibilitiesAfterBookingCompletedView extends VerticalLayout implements BeforeEnterObserver {

    private final Header header = new Header("Event Simulation", "Hier werden die Events simuliert, die nach dem Versenden der Buchungsbestätigung auftreten können. ");

    private final Button cancelBookingEvent = new Button("Kunde beantragt die Stornierung der Buchung");
    private final Button checkInPlusOneDayEvent = new Button("Die Anreise verschiebt sich um einen Tag");
    private final Button guestCheckInEvent = new Button("Der Gast möchte einchecken.");

    private final VerticalLayout view = new VerticalLayout(header, cancelBookingEvent, checkInPlusOneDayEvent, guestCheckInEvent);

    private final BookingRepo repo;

    public EventPossibilitiesAfterBookingCompletedView(BookingRepo repo) {
        this.repo = repo;
        add(view);

        this.addCancelBookingEventClickListener();
        this.addGuestCheckInEventClickListener();

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        GlobalState globalState = GlobalState.getInstance();
        if (globalState.getCurrentBooking() == null) {
            Routes.navigateTo(Routes.CUSTOMER_START);
        }
    }

    public void addCancelBookingEventClickListener() {
        this.cancelBookingEvent.addClickListener(event -> {
            GlobalState globalState = GlobalState.getInstance();
            globalState.getCurrentBooking().setBookingState(Booking.BookingState.Canceled);
            this.repo.save(globalState.getCurrentBooking());
            Routes.navigateTo(Routes.BOOKING_CANCELED);
        });
    }

    public void addGuestCheckInEventClickListener() {
        this.guestCheckInEvent.addClickListener(event -> {
            Routes.navigateTo(Routes.BOOKING_CHECK_IN);
        });
    }


}
