package com.anie.dara.trackmonbus_supir.model;

import android.os.Parcel;
import android.os.Parcelable;

public class rit implements Parcelable {
    String tgl,no_bus, jalur_id, jalur , waktu_berangkat, waktu_datang;



    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getNo_bus() {
        return no_bus;
    }

    public void setNo_bus(String no_bus) {
        this.no_bus = no_bus;
    }

    public String getJalur_id() {
        return jalur_id;
    }

    public void setJalur_id(String jalur_id) {
        this.jalur_id = jalur_id;
    }

    public String getJalur() {
        return jalur;
    }

    public void setJalur(String jalur) {
        this.jalur = jalur;
    }

    public String getWaktu_berangkat() {
        return waktu_berangkat;
    }

    public void setWaktu_berangkat(String waktu_berangkat) {
        this.waktu_berangkat = waktu_berangkat;
    }

    public String getWaktu_datang() {
        return waktu_datang;
    }

    public void setWaktu_datang(String waktu_datang) {
        this.waktu_datang = waktu_datang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tgl);
        dest.writeString(this.no_bus);
        dest.writeString(this.jalur_id);
        dest.writeString(this.jalur);
        dest.writeString(this.waktu_berangkat);
        dest.writeString(this.waktu_datang);
    }

    public rit() {
    }

    protected rit(Parcel in) {
        this.tgl = in.readString();
        this.no_bus = in.readString();
        this.jalur_id = in.readString();
        this.jalur = in.readString();
        this.waktu_berangkat = in.readString();
        this.waktu_datang = in.readString();
    }

    public static final Parcelable.Creator<rit> CREATOR = new Parcelable.Creator<rit>() {
        @Override
        public rit createFromParcel(Parcel source) {
            return new rit(source);
        }

        @Override
        public rit[] newArray(int size) {
            return new rit[size];
        }
    };
}
