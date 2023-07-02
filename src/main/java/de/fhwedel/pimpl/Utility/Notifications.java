package de.fhwedel.pimpl.Utility;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification;

public class Notifications {

    public static void showErrorNotification(String message) {
        Notification notification = new Notification(message, 1000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

}
