package com.anie.dara.trackmonbus_supir.model;

public class CheckpointHalte {
    Double lat,lng;
    String  halte;

    public CheckpointHalte(Double lat, Double lng, String halte) {
        this.lat = lat;
        this.lng = lng;
        this.halte = halte;
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

    public String getHalte() {
        return halte;
    }

    public void setHalte(String halte) {
        this.halte = halte;
    }
}
