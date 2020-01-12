package com.tehkotak.trailbleshooting;

public class DataModel {

    String datetime;
    String imageURL;
    String komentar;
    String latitude;
    String longitude;
    String name;


    public DataModel() {
    }

    public DataModel(String datetime, String imageURL, String komentar, String latitude, String longitude, String name) {
        this.datetime = datetime;
        this.imageURL = imageURL;
        this.komentar = komentar;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
