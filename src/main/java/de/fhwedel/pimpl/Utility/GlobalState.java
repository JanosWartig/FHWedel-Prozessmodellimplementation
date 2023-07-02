package de.fhwedel.pimpl.Utility;

import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.RoomCategory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class GlobalState {

    private static GlobalState globalState;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private Customer currentCustomer = null;
    private Booking currentBooking = null;
    private RoomCategory selectedRoomCategory = null;

    public static String SUPERVISOR_PROPERTY_NAME = "isSupervisorModeActive";
    public static String CURRENT_DATE_PROPERTY_NAME = "currentDate";
    private boolean isSupervisorModeActive = false;
    private LocalDate currentDate = LocalDate.now();

    private GlobalState() { }

    public static GlobalState getInstance() {
        if (globalState == null) {
            globalState = new GlobalState();
        }
        return globalState;
    }

    public void resetGlobalState() {
        currentCustomer = null;
        currentBooking = null;
        selectedRoomCategory = null;
        isSupervisorModeActive = false;
        currentDate = LocalDate.now();
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public RoomCategory getSelectedRoomCategory() {
        return selectedRoomCategory;
    }

    public void setSelectedRoomCategory(RoomCategory selectedRoomCategory) {
        this.selectedRoomCategory = selectedRoomCategory;
    }

    public Booking getCurrentBooking() {
        return currentBooking;
    }

    public void setCurrentBooking(Booking currentBooking) {
        this.currentBooking = currentBooking;
    }

    public void setSupervisorModeActive(boolean supervisorModeActive) {
        boolean oldValue = isSupervisorModeActive;
        isSupervisorModeActive = supervisorModeActive;
        propertyChangeSupport.firePropertyChange(SUPERVISOR_PROPERTY_NAME, oldValue, supervisorModeActive);
    }

    public boolean isSupervisorModeActive() {
        return this.isSupervisorModeActive;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        LocalDate oldValue = this.currentDate;
        this.currentDate = currentDate;
        propertyChangeSupport.firePropertyChange(CURRENT_DATE_PROPERTY_NAME, oldValue, currentDate);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
