package de.fhwedel.pimpl.Utility;

import java.util.UUID;

public class GenerateUniqueNumber {

    public static String createUniqueIdentifier() {
        return UUID.randomUUID().toString();
    }

}
