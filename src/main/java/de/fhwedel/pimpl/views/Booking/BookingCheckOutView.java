package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;

@Route(Routes.BOOKING_CHECK_OUT)
@SpringComponent
@UIScope
public class BookingCheckOutView extends VerticalLayout implements BeforeEnterObserver {

    private final Header header = new Header("Buchung auschecken", "Die Buchung des Kunden auschecken..");

    private final VerticalLayout view = new VerticalLayout(header);

    public BookingCheckOutView() {
        this.add(this.view);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (GlobalState.getInstance().getCurrentBooking() == null) {
            Routes.navigateTo(Routes.CUSTOMER_START);
        }
    }

}
