package com.tehkotak.trailbleshooting;

public class UploadInfo {

    public String komentar;
    public String imageURL;
    public String latitude;
    public String longitude;

    public UploadInfo() {
    }

    public UploadInfo(String name, String url, String latitude, String longitude) {
        this.komentar = name;
        this.imageURL = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getKomentar() {
        return komentar;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
