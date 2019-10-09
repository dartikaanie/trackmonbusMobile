package com.anie.dara.trackmonbus.model;

public class Posisi {

    Double lat,lng;
    String no_bus;

    public Posisi(Double lat, Double lng, String no_bus) {
        this.no_bus = no_bus;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNo_bus() {
        return no_bus;
    }

    public void setNo_bus(String no_bus) {
        this.no_bus = no_bus;
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

    @Override
    public String toString(){
        return
                "Bus{" +
                        "noBus = '" + no_bus +  "}";
    }

}
