package com.augmentedreality.simplus.firebase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.augmentedreality.simplus.Constants.FirebaseReference.SHIP_LOCATIONS;


/**
 * Utilities for accessing Firebase database.
 */
public class FirebaseManager {


    public static void updateShipLocation(String userUuid, double latitude, double longhitude) {
        DatabaseReference shipLocations = FirebaseDatabase.getInstance()
                                                            .getReference(SHIP_LOCATIONS);
        GeoFire geoFire = new GeoFire(shipLocations);
        geoFire.setLocation(userUuid,
                            new GeoLocation(latitude, longhitude),
                            (key, error) -> {

                            });
    }

}
