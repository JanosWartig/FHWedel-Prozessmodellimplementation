package de.fhwedel.pimpl.Utility;

import com.vaadin.flow.component.UI;

public class Routes {

    public static final String CUSTOMER_START = "/";
    public static final String CUSTOMER_CREATE = "/create-new-customer";
    public static final String CUSTOMER_UPDATE = "/update-customer";

    public static final String ROOM_START = "/select-room-category-and-booking-period";

    public static void navigateTo(String route) {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(route));
    }

}
