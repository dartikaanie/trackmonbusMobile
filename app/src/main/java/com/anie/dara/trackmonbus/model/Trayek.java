package com.anie.dara.trackmonbus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trayek implements Parcelable {

    String trayek_id;
    String trayek;
    Double km_rit;


    public Trayek(String trayek_id, String trayek, Double km_rit) {
        this.trayek_id = trayek_id;
        this.trayek = trayek;
        this.km_rit = km_rit;
    }

    public String getTrayek_id() {
        return trayek_id;
    }

    public void setTrayek_id(String trayek_id) {
        this.trayek_id = trayek_id;
    }

    public String getTrayek() {
        return trayek;
    }

    public void setTrayek(String trayek) {
        this.trayek = trayek;
    }

    public Double getKm_rit() {
        return km_rit;
    }

    public void setKm_rit(Double km_rit) {
        this.km_rit = km_rit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trayek_id);
        dest.writeString(this.trayek);
        dest.writeValue(this.km_rit);
    }

    protected Trayek(Parcel in) {
        this.trayek_id = in.readString();
        this.trayek = in.readString();
        this.km_rit = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Trayek> CREATOR = new Parcelable.Creator<Trayek>() {
        @Override
        public Trayek createFromParcel(Parcel source) {
            return new Trayek(source);
        }

        @Override
        public Trayek[] newArray(int size) {
            return new Trayek[size];
        }
    };
}
