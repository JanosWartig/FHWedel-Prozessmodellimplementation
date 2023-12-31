package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;

@Route(Routes.BOOKING_FINISHED)
@SpringComponent
@UIScope
public class BookingCanceledView extends Composite<Component> {

    private final PageLayout pageLayout = new PageLayout("Buchung abrechnen", "Dies ist das Ende des Buchungsprozesses.");

    public BookingCanceledView() {
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }
}
