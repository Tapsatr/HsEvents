package com.example.tr.hsevents;

/**
 * Created by T.R on 14/11/2017.
 */

public class DataModel {

    String name;
    String place;
    String startDate;
    String description;
    String endDate;
    String locUrl;
    String imageUrl;


    public DataModel(String name, String place, String startDate, String description, String endDate, String locUrl, String imageUrl ) {
        this.name=name;
        this.place=place;
        this.startDate=startDate;
        this.description=description;
        this.endDate=endDate;
        this.locUrl = locUrl;
        this.imageUrl = imageUrl;

    }


    public String getName() {
        return name;
    }


    public String getPlace() {
        return place;
    }


    public String getStartDate() {
        return startDate;
    }


    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLocUrl() { return locUrl;}

    public String getImageUrl() { return imageUrl;}

}
