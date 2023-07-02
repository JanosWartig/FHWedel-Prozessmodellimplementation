package de.fhwedel.pimpl.views.SimulateEvents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;

@SuppressWarnings("serial")
@Route(Routes.EVENTS_AFTER_BOOKING_COMPLETED)
@SpringComponent
@UIScope
public class EventPossibilitiesAfterBookingCompletedView extends VerticalLayout {

    private final Header header = new Header("Event Simulation",
            "Hier werden die Events simuliert, die nach dem Versenden der Buchungsbestätigung auftreten können. ", Constants.HEADLINE_1);

    private final Button cancelBookingEvent = new Button("Buchung stornieren");
    private final Button checkInPlusOneDayEvent = new Button("Die Anreise verschiebt sich um einen Tag");
    private final Button guestCheckInEvent = new Button("Gast hat eingecheckt");

    private final VerticalLayout view = new VerticalLayout(header, cancelBookingEvent, checkInPlusOneDayEvent, guestCheckInEvent);

    public EventPossibilitiesAfterBookingCompletedView() {
        add(view);
    }


}
