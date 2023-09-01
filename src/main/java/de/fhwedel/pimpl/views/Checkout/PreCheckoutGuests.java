package de.fhwedel.pimpl.views.Checkout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.Route;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.views.Guests.CheckInGuests;

@Route(Routes.PRE_CHECKOUT_ADDITIONAL_GUESTS)
public class PreCheckoutGuests extends Composite<Component> {

    private final PageLayout pageLayout = new PageLayout("Weitere Gäste einchecken", "Weitere Gäste überprüfen und Daten checken.");

    public PreCheckoutGuests(BookingRepo repo) {
        this.pageLayout.add(new CheckInGuests(repo));
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }
}
