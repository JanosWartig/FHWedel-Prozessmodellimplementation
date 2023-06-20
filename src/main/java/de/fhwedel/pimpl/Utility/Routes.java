package de.fhwedel.pimpl.Utility;

import com.vaadin.flow.component.UI;

public class Routes {

    public static final String CUSTOMER_START = "/";
    public static final String CUSTOMER_CREATE = "/create-new-customer";
    public static final String CUSTOMER_UPDATE = "/update-customer";

    public static void navigateTo(String route) {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(route));
    }

}
