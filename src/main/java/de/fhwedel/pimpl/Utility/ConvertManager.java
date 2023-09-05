package de.fhwedel.pimpl.Utility;

public class ConvertManager {

    public static int convertEuroToCent(Double euro) {
        return (int) Math.round(euro * 100);
    }

}
