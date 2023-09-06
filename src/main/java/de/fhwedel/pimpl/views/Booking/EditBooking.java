package de.fhwedel.pimpl.views.Booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.ConvertManager;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.repos.BookingRepo;

import java.util.Optional;

@Route(Routes.BOOKING_EDIT)
@SpringComponent
@UIScope
public class EditBooking extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();

    private final TextField bookingID = new TextField("Buchungs ID");
    private final TextField bookingNumber = new TextField("Buchungsnummer");
    private final DatePicker bookingDate = new DatePicker("Buchungsdatum");
    private final ComboBox<Booking.BookingState> bookingState = new ComboBox<>("Buchungsstatus");
    private final TextArea comment = new TextArea("Kommentar");
    private final DatePicker checkInShould = new DatePicker("Check In Soll");
    private final DatePicker checkInIs = new DatePicker("Check In Ist");
    private final DatePicker checkOutShould = new DatePicker("Check Out Soll");
    private final DatePicker checkOutIs = new DatePicker("Check Out Ist");
    private final NumberField roomPrice = new NumberField("Zimmerpreis in EUR");
    private final TextField kfz = new TextField("KFZ-Kennzeichen");

    HorizontalLayout nonChangingValues = new HorizontalLayout(bookingID, bookingNumber);
    HorizontalLayout secondRow = new HorizontalLayout(bookingDate, bookingState, comment);
    HorizontalLayout thirdRow = new HorizontalLayout(checkInShould, checkInIs, checkOutShould, checkOutIs);
    HorizontalLayout fourthRow = new HorizontalLayout(roomPrice, kfz);

    private final Button updateBooking = new Button("Buchung aktualisieren", event -> this.updateBooking());
    private final Button deleteBooking = new Button("Buchung löschen", event -> this.deleteBooking());

    private final HorizontalLayout buttons = new HorizontalLayout(updateBooking, deleteBooking);

    private final PageLayout pageLayout = new PageLayout("Buchung bearbeiten",
            "Hier kann die ausgewählte Buchung bearbeitet werden.",
            nonChangingValues, secondRow, thirdRow, fourthRow, buttons);

    private Booking currentBooking = null;
    private Booking.BookingState oldBookingState = null;
    private final BookingRepo repo;

    public EditBooking(BookingRepo repo) {
        this.repo = repo;
        this.initComboBox();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Optional<Booking> booking = this.repo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        this.currentBooking = booking.get();

        this.bookingID.setEnabled(false);
        this.bookingNumber.setEnabled(false);

        this.bookingID.setValue(this.currentBooking.getId().toString());
        this.bookingNumber.setValue(this.currentBooking.getBookingNumber());
        this.bookingDate.setValue(this.currentBooking.getBookingDate());
        this.bookingState.setValue(this.currentBooking.getBookingState());
        this.comment.setValue(this.currentBooking.getComment());
        this.checkInShould.setValue(this.currentBooking.getCheckInShould());
        if (this.currentBooking.getCheckInIs() != null) {
            this.checkInIs.setValue(this.currentBooking.getCheckInIs());
        }
        this.checkOutShould.setValue(this.currentBooking.getCheckOutShould());
        if (this.currentBooking.getCheckOutIs() != null) {
            this.checkOutIs.setValue(this.currentBooking.getCheckOutIs());
        }
        this.roomPrice.setValue(ConvertManager.convertCentToEuro(this.currentBooking.getRoomPrice()));
        if (this.currentBooking.getLicensePlate() != null) {
            this.kfz.setValue(this.currentBooking.getLicensePlate());
        }

        this.oldBookingState = this.currentBooking.getBookingState();
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initComboBox() {
        this.bookingState.setItems(Booking.BookingState.values());
    }

    private void deleteBooking() {
        this.repo.delete(this.currentBooking);
        Routes.navigateTo(Routes.BOOKINGS_SEARCH);
    }

    private void updateBooking() {
        this.currentBooking.setBookingDate(this.bookingDate.getValue());
        this.currentBooking.setBookingState(this.bookingState.getValue());
        this.currentBooking.setComment(this.comment.getValue());
        this.currentBooking.setCheckInShould(this.checkInShould.getValue());
        this.currentBooking.setCheckInIs(this.checkInIs.getValue());
        this.currentBooking.setCheckOutShould(this.checkOutShould.getValue());
        this.currentBooking.setCheckOutIs(this.checkOutIs.getValue());
        this.currentBooking.setRoomPrice(this.roomPrice.getValue().intValue());
        this.currentBooking.setLicensePlate(this.kfz.getValue());

        this.repo.save(this.currentBooking);

        if (this.oldBookingState != this.currentBooking.getBookingState()) {
            switch (this.currentBooking.getBookingState()) {
                case Reserved -> Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD);
                case Canceled, Canceled_Finished -> Routes.navigateTo(Routes.EVENTS_AFTER_BOOKING_COMPLETED);
                case CheckedIn -> Routes.navigateTo(Routes.BOOKING_CHECK_IN);
                case CheckedOut, Finished -> Routes.navigateTo(Routes.BOOKING_CHECK_OUT);
            }
            return;
        }

        Routes.navigateTo(Routes.BOOKINGS_SEARCH);
    }

}
