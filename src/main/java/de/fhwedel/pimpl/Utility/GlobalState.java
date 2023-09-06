package de.fhwedel.pimpl.Utility;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class GlobalState {

    private static GlobalState globalState;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private Integer currentCustomerID = -1;
    private Integer currentRoomCategoryID = -1;
    private LocalDate bookingCheckIn = null;
    private LocalDate bookingCheckOut = null;
    private Integer currentRoomID = -1;
    private Integer currentBookingID = -1;
    private Integer currentGuestID = -1;
    private LocalDate currentDate = LocalDate.now();








    public static String SUPERVISOR_PROPERTY_NAME = "isSupervisorModeActive";
    public static String CURRENT_DATE_PROPERTY_NAME = "currentDate";
    private boolean isSupervisorModeActive = false;


    private GlobalState() { }

    public static GlobalState getInstance() {
        if (globalState == null) {
            globalState = new GlobalState();
        }
        return globalState;
    }

    public int getCurrentCustomerID() {
    	return this.currentCustomerID;
    }

    public void setCurrentCustomerID(int currentCustomerID) {
    	this.currentCustomerID = currentCustomerID;
    }

    public int getCurrentRoomCategoryID() {
    	return this.currentRoomCategoryID;
    }

    public void setCurrentRoomCategoryID(int currentRoomCategoryID) {
    	this.currentRoomCategoryID = currentRoomCategoryID;
    }

    public LocalDate getBookingCheckIn() {
    	return this.bookingCheckIn;
    }

    public void setBookingCheckIn(LocalDate bookingCheckIn) {
    	this.bookingCheckIn = bookingCheckIn;
    }

    public LocalDate getBookingCheckOut() {
    	return this.bookingCheckOut;
    }

    public void setBookingCheckOut(LocalDate bookingCheckOut) {
    	this.bookingCheckOut = bookingCheckOut;
    }

    public int getCurrentRoomID() {
    	return this.currentRoomID;
    }

    public void setCurrentRoomID(int currentRoomID) {
    	this.currentRoomID = currentRoomID;
    }

    public int getCurrentBookingID() {
    	return this.currentBookingID;
    }

    public void setCurrentBookingID(int currentBookingID) {
    	this.currentBookingID = currentBookingID;
    }

    public int getCurrentGuestID() {
    	return this.currentGuestID;
    }

    public void setCurrentGuestID(int currentGuestID) {
    	this.currentGuestID = currentGuestID;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        LocalDate oldValue = this.currentDate;
        this.currentDate = currentDate;
        propertyChangeSupport.firePropertyChange(CURRENT_DATE_PROPERTY_NAME, oldValue, currentDate);
    }



    public void setSupervisorModeActive(boolean supervisorModeActive) {
        boolean oldValue = isSupervisorModeActive;
        isSupervisorModeActive = supervisorModeActive;
        propertyChangeSupport.firePropertyChange(SUPERVISOR_PROPERTY_NAME, oldValue, supervisorModeActive);
    }

    public boolean isSupervisorModeActive() {
        return this.isSupervisorModeActive;
    }



    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
