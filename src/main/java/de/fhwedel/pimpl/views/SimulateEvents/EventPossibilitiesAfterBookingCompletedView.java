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

@Route(Routes.EVENTS_AFTER_BOOKING_COMPLETED)
@SpringComponent
@UIScope
public class EventPossibilitiesAfterBookingCompletedView extends Composite<Component> implements BeforeEnterObserver {

    private final Button cancelBookingEvent = new Button("Kunde beantragt die Stornierung der Buchung", event -> handelCancelBookingEvent());
    private final Button checkInPlusOneDayEvent = new Button("Die Anreise verschiebt sich um einen Tag", event -> {

    });
    private final Button guestCheckInEvent = new Button("Der Gast möchte einchecken.", event -> Routes.navigateTo(Routes.BOOKING_CHECK_IN));
    private final PageLayout pageLayout = new PageLayout("Event Simulation",
            "Hier werden die Events simuliert, die nach dem Versenden der Buchungsbestätigung auftreten können.",
            cancelBookingEvent, checkInPlusOneDayEvent, guestCheckInEvent);
    private final BookingRepo repo;

    public EventPossibilitiesAfterBookingCompletedView(BookingRepo repo) {
        this.repo = repo;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        GlobalState globalState = GlobalState.getInstance();
        if (globalState.getCurrentBooking() == null) {
            Routes.navigateTo(Routes.SEARCH_CUSTOMER);
        }
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    public void handelCancelBookingEvent() {
        GlobalState globalState = GlobalState.getInstance();
        globalState.getCurrentBooking().setBookingState(Booking.BookingState.Canceled);
        this.repo.save(globalState.getCurrentBooking());
        Routes.navigateTo(Routes.BOOKING_CANCELED);
    }


}
