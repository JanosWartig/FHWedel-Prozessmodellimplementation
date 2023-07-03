package de.fhwedel.pimpl.Utility;

import com.vaadin.flow.component.UI;

public class Routes {

    public static final String CUSTOMER_START = "/";
    public static final String CUSTOMER_CREATE = "/create-new-customer";
    public static final String CUSTOMER_UPDATE = "/update-customer";

    public static final String SELECT_ROOM_START = "/select-room-category-and-booking-period";
    public static final String SELECT_ROOM_CHECK_AVAILABLE = "/check-available-rooms";
    public static final String SELECT_ROOM_BOOKING_FAILED = "/booking-failed";
    public static final String SELECT_ROOM_CALCULATE_PRICE = "/calculate-price";


    // Guests
    public static final String GUEST_ADD_GUEST = "/add-guests";


    // Events
    public static final String EVENTS_AFTER_BOOKING_COMPLETED = "/simulate-events-after-booking-completed";

    public static final String BOOKINGS_SEARCH = "/booking-search";
    public static final String BOOKING_CANCELED = "/booking-canceled";
    public static final String BOOKING_CHECK_IN = "/booking-check-in";
    public static final String BOOKING_CHECK_OUT = "/booking-check-out";

    public static void navigateTo(String route) {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(route));
    }

}
