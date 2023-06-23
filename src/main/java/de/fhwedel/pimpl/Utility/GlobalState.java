package de.fhwedel.pimpl.Utility;

import de.fhwedel.pimpl.model.Booking;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class GlobalState {

    private static GlobalState globalState;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private Integer currentCustomerID;
    private Booking currentBooking;
    private Integer selectedRoomCategoryID;

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

    public Integer getCurrentCustomerID() {
        return currentCustomerID;
    }

    public void setCurrentCustomerID(Integer currentCustomerID) {
        this.currentCustomerID = currentCustomerID;
    }

    public Integer getSelectedRoomCategoryID() {
        return selectedRoomCategoryID;
    }

    public void setSelectedRoomCategoryID(Integer selectedRoomCategoryID) {
        this.selectedRoomCategoryID = selectedRoomCategoryID;
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
