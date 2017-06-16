package com.harrisonseitz.guide2seattle;

/**
 * Created by harrisonseitz on 6/13/17.
 */

public class Location {

    // member variables for Location objects

    private String locationName;
    private String locationDescription;
    private int locationImage;
    private String locationHoursOpen;
    private String locationAddress;
    private String locationReserveString;

    public Location(String name, String description, int image, String hoursOpen, String address,
                    String reserveString) {
        locationName = name;
        locationDescription = description;
        locationImage = image;
        locationHoursOpen = hoursOpen;
        locationAddress = address;
        locationReserveString = reserveString;
    }

    public Location(String name, String description, int image, String hoursOpen, String address) {
        locationName = name;
        locationDescription = description;
        locationImage = image;
        locationHoursOpen = hoursOpen;
        locationAddress = address;
    }

    // Getter methods for Location objects
    public String getLocationName() { return locationName; }
    public String getLocationDescription() { return locationDescription; }
    public int getLocationImage() { return locationImage; }
    public String getLocationHoursOpen() { return locationHoursOpen; }
    public String getLocationAddress() { return locationAddress; }
    public String getLocationReserveString() { return locationReserveString; }

}
