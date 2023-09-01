package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.BackButton;
import de.fhwedel.pimpl.components.navigation.ErrorButton;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.text.DecimalFormat;

@Route(Routes.SELECT_ROOM_CALCULATE_PRICE)
@SpringComponent
@UIScope
public class CreateBooking extends Composite<Component> implements BeforeEnterObserver {

    Label priceDescription = new Label("Der Preis für das Zimmer beträgt: ");
    Label price = new Label();

    HorizontalLayout priceBox = new HorizontalLayout(priceDescription, price);
    TextArea comment = new TextArea("Kommentar");

    private final BackButton backButton = new BackButton("Zimmerkategorie bearbeiten", event -> Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD));

    private final ErrorButton errorButton = new ErrorButton("Abbrechen", event -> Routes.navigateTo(Routes.SELECT_ROOM_BOOKING_FAILED));

    private final ForwardButton forwardButton = new ForwardButton("Buchung anlegen", event -> this.createBooking());

    HorizontalLayout navigationLayout = new HorizontalLayout(backButton, errorButton, forwardButton);

    private final PageLayout pageLayout = new PageLayout("Buchung anlegen", "Hier wird die Buchung angelegt und der Preis bestimmt.",
            priceBox, comment, navigationLayout);

    private int calculatedPrice;
    private final BookingRepo bookingRepo;

    public CreateBooking(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
        this.forwardButton.setEnabled(true);
        this.initPrice();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RoomCategory roomCategory = GlobalState.getInstance().getSelectedRoomCategory();
        Customer customer = GlobalState.getInstance().getCurrentCustomer();

        this.calculatedPrice = Math.max(roomCategory.getPrice() * (1 - (customer.getDiscount() / 100)), roomCategory.getMinPrice());

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 €");
        String formattedPrice = decimalFormat.format(this.calculatedPrice);

        this.price.setText(formattedPrice);
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initPrice() {
        this.priceBox.setPadding(false);
        this.priceDescription.getStyle().set("font-size", "16px");
        this.price.getStyle().set("font-size", "30px");
        this.price.getStyle().set("font-weight", "bold");
    }

    private void createBooking() {
        GlobalState globalState = GlobalState.getInstance();
        globalState.getCurrentBooking().setRoomPrice(this.calculatedPrice);
        globalState.getCurrentBooking().setComment(this.comment.getValue());
        this.bookingRepo.save(globalState.getCurrentBooking());
        Routes.navigateTo(Routes.GUEST_ADD_GUEST);
    }

}
