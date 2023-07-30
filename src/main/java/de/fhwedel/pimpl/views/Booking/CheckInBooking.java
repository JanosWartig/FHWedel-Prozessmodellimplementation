package de.fhwedel.pimpl.views.Booking;

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
import de.fhwedel.pimpl.views.Guests.CheckInGuests;

@Route(Routes.BOOKING_CHECK_IN)
@SpringComponent
@UIScope
public class CheckInBooking extends VerticalLayout implements BeforeEnterObserver {

    private final BookingRepo repo;

    public CheckInBooking(BookingRepo repo) {
        this.repo = repo;
        Header header = new Header("Buchung und Gäste einchecken", "Gäste die eingeckeckt werden sollen, müssen korrekte Dokumente vorzeigen können.");
        VerticalLayout view = new VerticalLayout(header);
        this.add(view);
        this.add(new CheckInGuests(repo));
        Navigation navigation = new Navigation("Buchung einchecken", false);
        this.add(navigation);
        navigation.getForwardNavigation().addClickListener(e -> {
            CustomDialog customDialog = new CustomDialog("Die Buchung wurde erfolgreich eingecheckt.", "Fortfahren.");
            customDialog.open();
            customDialog.getCloseButton().addClickListener(event -> {
                GlobalState globalState = GlobalState.getInstance();
                globalState.getCurrentBooking().setBookingDate(globalState.getCurrentDate());
                globalState.getCurrentBooking().setBookingState(Booking.BookingState.CheckedIn);
                this.repo.save(globalState.getCurrentBooking());
                customDialog.getDialog().close();
                Routes.navigateTo(Routes.PRE_CHECKOUT_ADDITIONAL_GUESTS);
            });
        });
        navigation.getForwardNavigation().setEnabled(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) { if (GlobalState.getInstance().getCurrentBooking() == null) Routes.navigateTo(Routes.SEARCH_CUSTOMER); }

}
