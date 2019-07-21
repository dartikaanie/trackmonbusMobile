package com.anie.dara.trackmonbus.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Pesan implements Parcelable {

    String keluhan_id;
    String user_id;
    String perihal_id;
    String isi_keluhan;
    String created_at;
    String perihal;
    String name;
    Integer jumlah_komentar;

    public Pesan() {
    }

    public Pesan(String keluhan_id, String user_id, String perihal_id, String isi_keluhan, String created_at, String perihal, String name, Integer jumlah_komentar) {
        this.keluhan_id = keluhan_id;
        this.user_id = user_id;
        this.perihal_id = perihal_id;
        this.isi_keluhan = isi_keluhan;
        this.created_at = created_at;
        this.perihal = perihal;
        this.name = name;
        this.jumlah_komentar = jumlah_komentar;
    }

    public String getKeluhan_id() {
        return keluhan_id;
    }

    public void setKeluhan_id(String keluhan_id) {
        this.keluhan_id = keluhan_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPerihal_id() {
        return perihal_id;
    }

    public void setPerihal_id(String perihal_id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getJumlah_komentar() {
        return jumlah_komentar;
    }

    public void setJumlah_komentar(Integer jumlah_komentar) {
        this.jumlah_komentar = jumlah_komentar;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.perihal_id);
        dest.writeString(this.isi_keluhan);
        dest.writeString(this.created_at);
        dest.writeString(this.keluhan_id);
        dest.writeString(this.perihal);
        dest.writeString(this.name);
        dest.writeValue(this.jumlah_komentar);
    }

    protected Pesan(Parcel in) {
        this.user_id = in.readString();
        this.perihal_id = in.readString();
        this.isi_keluhan = in.readString();
        this.created_at = in.readString();
        this.keluhan_id = in.readString();
        this.perihal = in.readString();
        this.name = in.readString();
        this.jumlah_komentar = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Pesan> CREATOR = new Creator<Pesan>() {
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
