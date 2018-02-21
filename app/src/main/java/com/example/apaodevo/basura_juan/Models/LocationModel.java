package com.example.apaodevo.basura_juan.Models;

/**
 * Created by Brylle on 2/21/2018.
 */

public class LocationModel {
    private double latitude;
    private double longitude;
    public static LocationModel locationInstance;

    public static LocationModel getInstance(){
        if(locationInstance == null){
            return locationInstance = new LocationModel();
        }
        return locationInstance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
