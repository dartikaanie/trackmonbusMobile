package com.anie.dara.trackmonbus_supir.model;

public class CheckpointHalte {
    Double lat,lng;
    String  halte;

    public CheckpointHalte(Double lat, Double lng, String halte) {
        this.lat = lat;
        this.lng = lng;
        this.halte = halte;
    }
}
