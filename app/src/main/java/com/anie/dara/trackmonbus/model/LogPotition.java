package com.anie.dara.trackmonbus.model;

public class LogPotition {
    Double lat,lng;
    String InArea, user;

    public LogPotition(Double lat, Double lng, String inArea, String user) {
        this.lat = lat;
        this.lng = lng;
        InArea = inArea;
        this.user = user;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getInArea() {
        return InArea;
    }

    public void setInArea(String inArea) {
        InArea = inArea;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
