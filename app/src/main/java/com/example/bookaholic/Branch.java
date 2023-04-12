package com.example.bookaholic;

import java.util.ArrayList;

public class Branch {
    private String name, phoneNumber, openTime, closeTime;
    private double latitude, longitude;
    private float rating;
    private int H_open, M_open, H_close, M_close;

    public Branch() {
    }

    public Branch(String name, String phoneNumber, String openTime, String closeTime, double latitude, double longitude, float rating) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Branch clone() {
        return new Branch(name, phoneNumber, openTime, closeTime, latitude, longitude, rating);
    }

    private boolean isOpening(int currentH, int currentM) {
        if (this.H_open < currentH && currentH < this.H_close) {
            return true;
        }
        else if (this.H_open == currentH) {
            return this.M_open <= currentM;
        }
        else if (this.H_close == currentM) {
            return this.M_close > currentM;
        }
        return false;
    }

    public ArrayList<String> getFormat(int currentH, int currentM) {
        ArrayList<String> res = new ArrayList<>();

        boolean isOpen = isOpening(currentH, currentM);

        if (isOpen) {
            res.add("Open");
            res.add("Closes " + this.closeTime);
        }
        else {
            res.add("Closed");
            res.add("Opens " + this.openTime);
        }

        return res;
    }
}

