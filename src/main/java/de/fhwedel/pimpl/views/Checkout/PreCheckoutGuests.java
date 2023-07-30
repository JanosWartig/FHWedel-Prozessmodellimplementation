package de.fhwedel.pimpl.views.Checkout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.views.Guests.CheckInGuests;

@Route(Routes.PRE_CHECKOUT_ADDITIONAL_GUESTS)
public class PreCheckoutGuests extends VerticalLayout {

    public PreCheckoutGuests(BookingRepo repo) {
        Header header = new Header("Weitere Gäste einchecken", "Weitere Gäste überprüfen und Daten checken.");
        this.add(header);
        this.add(new CheckInGuests(repo));
    }
}
