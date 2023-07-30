package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

@Route(Routes.GUEST_UPDATE)
public class UpdateGuest extends VerticalLayout implements BeforeEnterObserver {

    private Guest guest;

    private final TextField guestName = new TextField();
    private final TextField guestFirstName = new TextField();
    private final DatePicker guestBirthDate = new DatePicker();

    private BookingRepo repo;
    public UpdateGuest(BookingRepo repo) {
        this.repo = repo;
        Header header = new Header("Gast aktualisieren", "Aktualisiere die Daten des Gastes");
        Button updateGuest = new Button("Gast aktualisieren", event -> updateGuest());
        Button backButton = new Button("Zurück", event -> back());
        HorizontalLayout buttons = new HorizontalLayout(backButton, updateGuest);
        FormLayout customerForm = new FormLayout(guestFirstName, guestName, guestBirthDate);
        customerForm.setWidth("700px");
        this.setPadding(true);
        this.setSpacing(true);

        this.add(header, customerForm, buttons);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.guest = GlobalState.getInstance().getCurrentGuest();
        this.guestName.setValue(this.guest.getName());
        this.guestFirstName.setValue(this.guest.getFirstName());
        this.guestBirthDate.setValue(this.guest.getBirthDate());
    }

    private void updateGuest() {
        GlobalState.getInstance().getCurrentGuest().setName(this.guestName.getValue());
        GlobalState.getInstance().getCurrentGuest().setFirstName(this.guestFirstName.getValue());
        GlobalState.getInstance().getCurrentGuest().setBirthDate(this.guestBirthDate.getValue());

        GlobalState.getInstance().getCurrentBooking().updateGuest(GlobalState.getInstance().getCurrentGuest());
        this.repo.save(GlobalState.getInstance().getCurrentBooking());

        Routes.navigateTo(Routes.BOOKING_CHECK_IN);
    }

    private void back() {
        Routes.navigateTo(Routes.BOOKING_CHECK_IN);
    }


}
