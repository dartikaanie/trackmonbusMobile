package com.anie.dara.trackmonbus.model;

import java.util.Date;

public class Pesan {

    int keluhan_id;
    int user_id;
    int perihal_id;
    String isi_keluhan;
    String created_at;
    String perihal;

    public Pesan(int keluhan_id, int user_id, int perihal_id, String isi_keluhan, String created_at) {
        this.keluhan_id = keluhan_id;
        this.user_id = user_id;
        this.perihal_id = perihal_id;
        this.isi_keluhan = isi_keluhan;
        this.created_at = created_at;
    }
    public Pesan() {

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
}
