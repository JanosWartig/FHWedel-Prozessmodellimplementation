package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.AllowedToPerformActionManager;
import de.fhwedel.pimpl.Utility.ConvertManager;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Notifications;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.BackButton;
import de.fhwedel.pimpl.components.navigation.ErrorButton;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.repos.CustomerRepo;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;
import de.fhwedel.pimpl.repos.RoomRepo;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;

@Route(Routes.SELECT_ROOM_CALCULATE_PRICE)
@SpringComponent
@UIScope
public class CreateBooking extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final TextField roomCategoryName = new TextField();
    private final TextField roomCategoryPrice = new TextField();
    private final TextField roomCategoryPriceMin = new TextField();
    private final TextField roomPrice = new TextField();


    private final TextField checkIn = new TextField();
    private final TextField checkOut = new TextField();

    private final FormLayout roomPriceForm = new FormLayout();

    private final TextArea comment = new TextArea("Kommentar");

    private final BackButton backButton = new BackButton("Zimmerkategorie bearbeiten", event -> Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD));

    private final ErrorButton errorButton = new ErrorButton("Abbrechen", event -> Routes.navigateTo(Routes.SELECT_ROOM_BOOKING_FAILED));

    private final ForwardButton forwardButton = new ForwardButton("Buchung anlegen", event -> this.createBooking());

    HorizontalLayout navigationLayout = new HorizontalLayout(backButton, errorButton, forwardButton);

    private final PageLayout pageLayout = new PageLayout("Buchung anlegen", "Hier wird die Buchung angelegt und der Preis bestimmt.",
            roomPriceForm, comment, navigationLayout);

    private Customer currentCustomer;
    private Room currentRoom;
    private final BookingRepo bookingRepo;
    private final RoomCategoryRepo roomCategoryRepo;
    private final CustomerRepo customerRepo;

    private final RoomRepo roomRepo;

    public CreateBooking(BookingRepo bookingRepo, RoomCategoryRepo roomCategoryRepo, CustomerRepo customerRepo, RoomRepo roomRepo) {
        this.bookingRepo = bookingRepo;
        this.roomCategoryRepo = roomCategoryRepo;
        this.customerRepo = customerRepo;
        this.roomRepo = roomRepo;
        this.forwardButton.setEnabled(true);
        this.initPrice();
        this.roomPrice.setEnabled(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (globalState.getCurrentCustomerID() == -1 || globalState.getCurrentRoomID() == -1 || globalState.getCurrentRoomCategoryID() == -1) {
            Routes.navigateTo(Routes.SEARCH_CUSTOMER);
            return;
        }

        Optional<RoomCategory> roomCategory = roomCategoryRepo.findById(globalState.getCurrentRoomCategoryID());
        Optional<Customer> customer = this.customerRepo.findById(globalState.getCurrentCustomerID());
        Optional<Room> room = this.roomRepo.findById(globalState.getCurrentRoomID());
        String checkIn = globalState.getBookingCheckIn().toString();
        String checkOut = globalState.getBookingCheckOut().toString();

        if (roomCategory.isEmpty() || customer.isEmpty() || room.isEmpty()) {
            Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD);
            return;
        }

        this.currentCustomer = customer.get();
        this.currentRoom = room.get();

        this.roomCategoryName.setValue(roomCategory.get().getName());
        this.roomCategoryPrice.setValue(roomCategory.get().getPrice() + " €");
        this.roomCategoryPriceMin.setValue(roomCategory.get().getMinPrice() + " €");
        this.checkIn.setValue(checkIn);
        this.checkOut.setValue(checkOut);

        this.roomCategoryName.setEnabled(false);
        this.roomCategoryPrice.setEnabled(false);
        this.roomCategoryPriceMin.setEnabled(false);
        this.checkIn.setEnabled(false);
        this.checkOut.setEnabled(false);

        int computedPrice = Math.max(roomCategory.get().getPrice() * (1 - (customer.get().getDiscount() / 100)), roomCategory.get().getMinPrice());
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 €");
        String formattedPrice = decimalFormat.format(computedPrice);
        this.roomPrice.setValue(formattedPrice);
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initPrice() {
        this.roomPriceForm.addFormItem(this.roomCategoryName, "Zimmerkategorie");
        this.roomPriceForm.addFormItem(this.roomCategoryPrice, "Preis");
        this.roomPriceForm.addFormItem(this.roomCategoryPriceMin, "Mindestpreis");
        this.roomPriceForm.addFormItem(this.roomPrice, "Errechneter Zimmerpreis");
        this.roomPriceForm.addFormItem(this.checkIn, "Check-In");
        this.roomPriceForm.addFormItem(this.checkOut, "Check-Out");
    }

    private void createBooking() {
        int convertedPrice = this.convertStringEuroToNumber();

        Booking newBooking = new Booking(globalState.getCurrentDate(),
                Booking.BookingState.Reserved,
                this.comment.getValue(),
                this.globalState.getBookingCheckIn(),
                null,
                this.globalState.getBookingCheckOut(),
                null,
                convertedPrice,
                null,
                this.currentCustomer,
                this.currentRoom
        );

        if (!this.isAllowedToCreateBooking(newBooking)) return;

        Booking booking = this.bookingRepo.save(newBooking);
        String bookingNumber = "booking_" + booking.getId();
        booking.setBookingNumber(bookingNumber);
        this.bookingRepo.save(booking);
        globalState.setCurrentBookingID(booking.getId());
        Routes.navigateTo(Routes.GUEST_ADD_GUEST);
    }

    public int convertStringEuroToNumber() {
        String price = this.roomPrice.getValue();
        price = price.replaceAll("[^0-9.,]", "");
        price = price.replace(',', '.');
        try {
            double euroAmount = Double.parseDouble(price);
            return ConvertManager.convertEuroToCent(euroAmount);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isAllowedToCreateBooking(Booking booking) {
        // Nicht Supervisor dann muss der Zimmerkategorie.Mindestpreis >= Preis sein
        if (this.convertStringEuroToNumber() < this.currentRoom.getRoomCategory().getMinPrice() * 100 && !globalState.isSupervisorModeActive()) {
            Notifications.showErrorNotification("Der Preis liegt unter dem Mindestpreis der Zimmerkategorie.");
            return false;
        }

        LocalDate checkIn = globalState.getBookingCheckIn();
        LocalDate checkOut = globalState.getBookingCheckOut();
        LocalDate currentDate = globalState.getCurrentDate();

        boolean isValidBookingPeriod = AllowedToPerformActionManager.isValidBookingPeriod(checkIn, checkOut, currentDate);
        boolean isAllowedToCreateNewBooking = AllowedToPerformActionManager.isAllowedToCreateNewBooking(booking, currentDate);

        return isValidBookingPeriod && isAllowedToCreateNewBooking;
    }

}
