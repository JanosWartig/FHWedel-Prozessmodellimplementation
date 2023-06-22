package de.fhwedel.pimpl.Utility;

public class GlobalState {

    private static GlobalState globalState;

    private Integer currentCustomerID;
    private Integer selectedRoomCategoryID;

    private GlobalState() {
        // Initialisierung der Singleton-Klasse
    }

    // Öffentliche statische Methode zum Abrufen der Singleton-Instanz
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

}
