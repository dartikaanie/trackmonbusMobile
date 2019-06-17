package com.anie.dara.trackmonbus.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Pesan implements Parcelable {

    int keluhan_id;
    int user_id;
    int perihal_id;
    String isi_keluhan;
    String created_at;
    String perihal;
    String name;


    public Pesan() {

    }

    public Pesan(int keluhan_id, int user_id, int perihal_id, String isi_keluhan, String created_at, String perihal, String name) {
        this.keluhan_id = keluhan_id;
        this.user_id = user_id;
        this.perihal_id = perihal_id;
        this.isi_keluhan = isi_keluhan;
        this.created_at = created_at;
        this.perihal = perihal;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKeluhan_id() {
        return keluhan_id;
    }

    public void setKeluhan_id(int keluhan_id) {
        this.keluhan_id = keluhan_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPerihal_id() {
        return perihal_id;
    }

    public void setPerihal_id(int perihal_id) {
        this.perihal_id = perihal_id;
    }

    public String getIsi_keluhan() {
        return isi_keluhan;
    }

    public void setIsi_keluhan(String isi_keluhan) {
        this.isi_keluhan = isi_keluhan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPerihal() {
        return perihal;
    }

    public void setPerihal(String perihal) {
        this.perihal = perihal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.keluhan_id);
        dest.writeInt(this.user_id);
        dest.writeInt(this.perihal_id);
        dest.writeString(this.isi_keluhan);
        dest.writeString(this.created_at);
        dest.writeString(this.perihal);
        dest.writeString(this.name);
    }

    protected Pesan(Parcel in) {
        this.keluhan_id = in.readInt();
        this.user_id = in.readInt();
        this.perihal_id = in.readInt();
        this.isi_keluhan = in.readString();
        this.created_at = in.readString();
        this.perihal = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Pesan> CREATOR = new Parcelable.Creator<Pesan>() {
        @Override
        public Pesan createFromParcel(Parcel source) {
            return new Pesan(source);
        }

        @Override
        public Pesan[] newArray(int size) {
            return new Pesan[size];
        }
    };
}
