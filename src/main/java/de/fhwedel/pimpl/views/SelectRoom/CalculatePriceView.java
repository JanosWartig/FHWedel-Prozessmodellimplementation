package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.CustomerRepo;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;

import java.text.DecimalFormat;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.SELECT_ROOM_CALCULATE_PRICE)
@SpringComponent
@UIScope
public class CalculatePriceView extends VerticalLayout implements BeforeEnterObserver {

    Header header = new Header("Preis bestimmen",
            "Hier wird der Preis für den Kunden berechnet.", Constants.HEADLINE_1);

    Label priceDescription = new Label("Der Preis für das Zimmer beträgt: ");
    Label price = new Label();

    HorizontalLayout priceBox = new HorizontalLayout(priceDescription, price);

    Navigation navigation = new Navigation("Zimmerkategorie bearbeiten", "Buchung anlegen", "Abbrechen");

    private VerticalLayout view = new VerticalLayout(header, priceBox, navigation);

    private CustomerRepo customerRepo;
    private RoomCategoryRepo roomCategoryRepo;

    public CalculatePriceView(CustomerRepo customerRepo, RoomCategoryRepo roomCategoryRepo) {
        this.customerRepo = customerRepo;
        this.roomCategoryRepo = roomCategoryRepo;

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
        Integer roomCategoryID = GlobalState.getInstance().getSelectedRoomCategoryID();
        Integer customerID = GlobalState.getInstance().getCurrentCustomerID();

        Optional<RoomCategory> roomCategoryOptional = this.roomCategoryRepo.findById(roomCategoryID);
        Optional<Customer> customerOptional = this.customerRepo.findById(customerID);

        if (roomCategoryOptional.isPresent() && customerOptional.isPresent()) {
            RoomCategory roomCategory = roomCategoryOptional.get();
            Customer customer = customerOptional.get();

            int price = Math.max(roomCategory.getPrice() * (1 - (customer.getDiscount() / 100)), roomCategory.getMinPrice());

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 €");
            String formattedPrice = decimalFormat.format(price);


            this.price.setText(formattedPrice);
        }
    }

    private void createBooking() {

    }




}
