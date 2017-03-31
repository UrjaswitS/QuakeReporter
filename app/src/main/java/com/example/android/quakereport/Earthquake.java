package com.example.android.quakereport;

/**
 * Created by UrJasWitK on 03-Mar-17.
 */

public class Earthquake {

    private String eMagnitude, eLocation, eDate;

    public Earthquake(String magnitude, String location,
                                String date)
    {
        eMagnitude = magnitude;
        eLocation = location;
        eDate = date;
    }

    public String geteDate() {
        return eDate;
    }

    public String geteLocation() {
        return eLocation;
    }

    public String geteMagnitude() {
        return eMagnitude;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public void seteLocation(String eLocation) {
        this.eLocation = eLocation;
    }

    public void seteMagnitude(String eMagnitude) {
        this.eMagnitude = eMagnitude;
    }
}
