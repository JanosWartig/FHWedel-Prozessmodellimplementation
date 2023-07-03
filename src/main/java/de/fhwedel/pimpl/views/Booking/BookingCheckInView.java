package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.CustomDialog;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.views.Guests.GuestCheckInView;

@Route(Routes.BOOKING_CHECK_IN)
@SpringComponent
@UIScope
public class BookingCheckInView extends VerticalLayout implements BeforeEnterObserver {

    private final Header header = new Header("Buchung einchecken", "Die Buchung des Kunden einchecken.");
    private final Checkbox guestsShowedPersoAndValidatedData = new Checkbox("Gäste haben Perso gezeigt und Daten überprüft");
    private final Navigation navigation = new Navigation("Alle Daten korrekt und Buchung einchecken", false);

    private final VerticalLayout view = new VerticalLayout(header);

    private final BookingRepo repo;

    public BookingCheckInView(BookingRepo repo) {
        this.repo = repo;
        this.add(this.view);
        this.add(new GuestCheckInView());
        this.add(this.guestsShowedPersoAndValidatedData);
        this.add(this.navigation);
        this.checkBoxListener();
        this.navigation.getFinish().addClickListener(e -> {
            CustomDialog customDialog = new CustomDialog("Buchung eingecheckt", "Die Buchung wurde erfolgreich eingecheckt.");
            customDialog.open();
            customDialog.getCloseButton().addClickListener(event -> {
                GlobalState globalState = GlobalState.getInstance();
                globalState.getCurrentBooking().setBookingState(Booking.BookingState.CheckedIn);
                this.repo.save(globalState.getCurrentBooking());
                Routes.navigateTo(Routes.BOOKING_CHECK_OUT);
            });
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (GlobalState.getInstance().getCurrentBooking() == null) {
            Routes.navigateTo(Routes.CUSTOMER_START);
        }
    }

    public void checkBoxListener() {
        this.guestsShowedPersoAndValidatedData.addValueChangeListener(event -> {
            this.navigation.getFinish().setEnabled(event.getValue());
        });
    }

}
