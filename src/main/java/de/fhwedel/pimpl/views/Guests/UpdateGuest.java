package de.fhwedel.pimpl.views.Guests;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Guest;
import de.fhwedel.pimpl.repos.BookingRepo;

@Route(Routes.GUEST_UPDATE)
public class UpdateGuest extends Composite<Component> implements BeforeEnterObserver {

    private final TextField guestName = new TextField();
    private final TextField guestFirstName = new TextField();
    private final DatePicker guestBirthDate = new DatePicker();
    private final FormLayout guestForm = new FormLayout(guestFirstName, guestName, guestBirthDate);
    private final Button updateGuest = new Button("Gast aktualisieren", event -> updateGuest());
    private final Button backButton = new Button("Zurück", event -> Routes.navigateTo(Routes.BOOKING_CHECK_IN));
    private final HorizontalLayout navigation = new HorizontalLayout(backButton, updateGuest);
    private final PageLayout pageLayout = new PageLayout("Gast aktualisieren", "Aktualisiere die Daten des Gastes", guestForm, navigation);
    private final BookingRepo repo;

    public UpdateGuest(BookingRepo repo) {
        this.repo = repo;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Guest guest = GlobalState.getInstance().getCurrentGuest();
        this.guestName.setValue(guest.getName());
        this.guestFirstName.setValue(guest.getFirstName());
        this.guestBirthDate.setValue(guest.getBirthDate());
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void updateGuest() {
        GlobalState.getInstance().getCurrentGuest().setName(this.guestName.getValue());
        GlobalState.getInstance().getCurrentGuest().setFirstName(this.guestFirstName.getValue());
        GlobalState.getInstance().getCurrentGuest().setBirthDate(this.guestBirthDate.getValue());
        GlobalState.getInstance().getCurrentBooking().updateGuest(GlobalState.getInstance().getCurrentGuest());
        this.repo.save(GlobalState.getInstance().getCurrentBooking());
        Routes.navigateTo(Routes.BOOKING_CHECK_IN);
    }
}
