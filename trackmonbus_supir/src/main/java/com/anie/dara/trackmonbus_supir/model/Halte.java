package com.anie.dara.trackmonbus_supir.model;

public class Halte {
    String halte_id;
    String nama;
    String lat;
    String lng;

    public Halte() {
    }

    public Halte(String halte_id,String nama, String lat, String lng) {
        this.nama = nama;
        this.lat = lat;
        this.lng = lng;
        this.halte_id = halte_id;
    }

    public String getHalte_id() {
        return halte_id;
    }

    public void setHalte_id(String halte_id) {
        this.halte_id = halte_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
