package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;

@Route(Routes.BOOKING_CANCELED)
@SpringComponent
@UIScope
public class BookingCanceledView extends VerticalLayout {

    private final Header header = new Header("Buchung storniert", "Die Buchung wurde erfolgreich storniert.");

    private final VerticalLayout view = new VerticalLayout(header);

    public BookingCanceledView() {
        this.add(this.view);
    }

}
