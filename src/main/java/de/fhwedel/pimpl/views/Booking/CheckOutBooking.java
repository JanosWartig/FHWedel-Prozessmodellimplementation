package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;

@Route(Routes.BOOKING_CHECK_OUT)
@SpringComponent
@UIScope
public class CheckOutBooking extends Composite<Component> implements BeforeEnterObserver {

    private final PageLayout pageLayout = new PageLayout("Buchung auschecken", "Die Buchung des Kunden auschecken.");

    public CheckOutBooking() {}

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (GlobalState.getInstance().getCurrentBooking() == null) Routes.navigateTo(Routes.SEARCH_CUSTOMER);
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }
}
