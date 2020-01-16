package com.anie.dara.trackmonbus_supir.model;

public class PosisiTime {
    Double lat,lng;
    String time;

    public PosisiTime(Double lat, Double lng, String time) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean cek(Double cekLat, Double cekLng){
        if((this.lat.equals(cekLat)) && (this.lng.equals(cekLng))){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "PosisiTime{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", time='" + time + '\'' +
                '}';
    }
}
