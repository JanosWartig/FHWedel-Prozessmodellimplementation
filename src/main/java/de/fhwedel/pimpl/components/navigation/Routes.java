package de.fhwedel.pimpl.components.navigation;

import com.vaadin.flow.component.UI;

public class Routes {

    public static final String SEARCH_CUSTOMER = "/";
    public static final String CREATE_CUSTOMER = "/create-new-customer";
    public static final String UPDATE_CUSTOMER = "/update-customer";

    public static final String SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD = "/select-room-category-and-booking-period";
    public static final String SELECT_ROOM_CHECK_AVAILABLE = "/check-available-rooms";
    public static final String SELECT_ROOM_BOOKING_FAILED = "/booking-failed";
    public static final String SELECT_ROOM_CALCULATE_PRICE = "/calculate-price";


    // Guests
    public static final String GUEST_ADD_GUEST = "/add-guests";
    public static final String GUEST_UPDATE = "/update-guest";

    // Checkout
    public static final String PRE_CHECKOUT_ADDITIONAL_GUESTS = "/pre-checkout-additional-guests";

    // Events
    public static final String EVENTS_AFTER_BOOKING_COMPLETED = "/simulate-events-after-booking-completed";

    public static final String BOOKINGS_SEARCH = "/booking-search";
    public static final String BOOKING_FINISHED = "/booking-finished";
    public static final String BOOKING_CHECK_IN = "/booking-check-in";
    public static final String BOOKING_CHECK_OUT = "/booking-check-out";

    public static final String BOOKING_EDIT = "/booking-edit";

    public static void navigateTo(String route) {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(route));
    }

}
