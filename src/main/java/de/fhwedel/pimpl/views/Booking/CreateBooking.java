package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.repos.CustomerRepo;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;

import java.text.DecimalFormat;

@SuppressWarnings("serial")
@Route(Routes.SELECT_ROOM_CALCULATE_PRICE)
@SpringComponent
@UIScope
public class CreateBooking extends VerticalLayout implements BeforeEnterObserver {

    Header header = new Header("Buchung anlegen", "Hier wird die Buchung angelegt und der Preis bestimmt.");

    Label priceDescription = new Label("Der Preis für das Zimmer beträgt: ");
    Label price = new Label();

    HorizontalLayout priceBox = new HorizontalLayout(priceDescription, price);
    TextArea comment = new TextArea("Kommentar");

    Navigation navigation = new Navigation("Zimmerkategorie bearbeiten", "Buchung anlegen", "Abbrechen");

    private int calculatedPrice;

    private VerticalLayout view = new VerticalLayout(header, priceBox, comment, navigation);

    private CustomerRepo customerRepo;
    private RoomCategoryRepo roomCategoryRepo;
    private BookingRepo bookingRepo;

    public CreateBooking(CustomerRepo customerRepo, RoomCategoryRepo roomCategoryRepo, BookingRepo bookingRepo) {
        this.customerRepo = customerRepo;
        this.roomCategoryRepo = roomCategoryRepo;
        this.bookingRepo = bookingRepo;

        this.priceBox.setPadding(false);
        this.priceDescription.getStyle().set("font-size", "16px");
        this.price.getStyle().set("font-size", "30px");
        this.price.getStyle().set("font-weight", "bold");

        this.navigation.getEdit().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_START));
        this.navigation.getCancel().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_BOOKING_FAILED));
        this.navigation.getFinish().addClickListener(event -> {

            this.createBooking();

        });

        this.add(view);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RoomCategory roomCategory = GlobalState.getInstance().getSelectedRoomCategory();
        Customer customer = GlobalState.getInstance().getCurrentCustomer();

        System.out.println(customer);
        System.out.println(customer.getDiscount());

        this.calculatedPrice = Math.max(roomCategory.getPrice() * (1 - (customer.getDiscount() / 100)), roomCategory.getMinPrice());

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 €");
        String formattedPrice = decimalFormat.format(this.calculatedPrice);


        this.price.setText(formattedPrice);
    }

    private void createBooking() {
        GlobalState globalState = GlobalState.getInstance();
        globalState.getCurrentBooking().setRoomPrice(this.calculatedPrice);
        globalState.getCurrentBooking().setComment(this.comment.getValue());
        this.bookingRepo.save(globalState.getCurrentBooking());
        Routes.navigateTo(Routes.GUEST_ADD_GUEST);
    }


}
