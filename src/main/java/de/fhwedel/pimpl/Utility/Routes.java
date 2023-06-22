package de.fhwedel.pimpl.Utility;

import com.vaadin.flow.component.UI;

public class Routes {

    public static final String CUSTOMER_START = "/";
    public static final String CUSTOMER_CREATE = "/create-new-customer";
    public static final String CUSTOMER_UPDATE = "/update-customer";

    public static final String ROOM_START = "/select-room-category-and-booking-period";
    public static final String ROOM_AVAILABLE = "/check-available-rooms";
    public static final String ROOM_NOT_FOUND = "/no-rooms";
    public static final String ROOM_CALCULATE_PRICE = "/calculate-price";

    public static void navigateTo(String route) {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(route));
    }

}
