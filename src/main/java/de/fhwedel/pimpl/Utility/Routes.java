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


    public static final String BOOKINGS_SEARCH = "/booking-search";

    public static void navigateTo(String route) {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(route));
    }

}
