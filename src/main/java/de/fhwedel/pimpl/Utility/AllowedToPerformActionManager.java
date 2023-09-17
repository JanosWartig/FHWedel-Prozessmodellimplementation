package de.fhwedel.pimpl.Utility;

import de.fhwedel.pimpl.model.Booking;

import java.time.LocalDate;

public class AllowedToPerformActionManager {

    public static boolean isValidBookingPeriod(LocalDate checkIn, LocalDate checkOut, LocalDate currentDate) {
        // Überprüfung: checkInDate >= today
        if (checkIn.isBefore(currentDate)) {
            Notifications.showErrorNotification("Das Check-In Datum liegt in der Vergangenheit.");
            return false;
        }

        // Überprüfung: checkOutDate > checkInDate
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            Notifications.showErrorNotification("Das Check-Out Datum liegt vor dem Check-In Datum.");
            return false;
        }

        // Überprüfung: checkOutDate <= checkInDate + 14 Tage
        LocalDate maxCheckOutDate = checkIn.plusDays(14);
        if (checkOut.isAfter(maxCheckOutDate) && !GlobalState.getInstance().isSupervisorModeActive()) {
            Notifications.showErrorNotification("Die Zeitspanne zwischen Check-In und Check-Out beträgt mehr als 14 Tage.");
            return false;
        }

        return true;
    }

    public static boolean isAllowedToCreateNewBooking(Booking booking, LocalDate currentDate) {

        if (!booking.getBookingDate().isEqual(currentDate)) {
            Notifications.showErrorNotification("Beim neu erstellen einer Buchung muss das Buchungsdatum dem aktuellen Datum entsprechen, das ist momentan nicht der Fall.");
            return false;
        }

        System.out.println("Das ist der Status der Buchung: " + booking.getBookingState());
        System.out.println("Hiermit setze ich den Status der Buchung auf Reserviert:" + Booking.BookingState.Reserved);

        /**
        if (booking.getBookingState().toString() == Booking.BookingState.Reserved.toString()) {
            Notifications.showErrorNotification("Beim neu erstellen einer Buchung muss der Status der Buchung auf Reserviert stehen, das ist momentan nicht der Fall.");
            return false;
        } */

        if (booking.getCheckInShould() == null) {
            Notifications.showErrorNotification("Beim neu erstellen einer Buchung muss das Check-In Datum gesetzt sein, das ist momentan nicht der Fall.");
            return false;
        }

        if (booking.getCheckOutShould() == null) {
            Notifications.showErrorNotification("Beim neu erstellen einer Buchung muss das Check-Out Datum gesetzt sein, das ist momentan nicht der Fall.");
            return false;
        }

        if (booking.getRoomPrice() == null) {
            Notifications.showErrorNotification("Beim neu erstellen einer Buchung muss der Zimmerpreis gesetzt sein, das ist momentan nicht der Fall.");
            return false;
        }

        return true;
    }

}
