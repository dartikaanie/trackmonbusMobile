package com.anie.dara.trackmonbus.model;

public class Komentar {
    String keluhan_id, name, isi_komentar, created_at, user_id, keluhan_komentar_id;

    public String getKeluhan_komentar_id() {
        return keluhan_komentar_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getKeluhan_id() {
        return keluhan_id;
    }

    public String getName() {
        return name;
    }

    public String getIsi_komentar() {
        return isi_komentar;
    }

    public String getCreated_at() {
        return created_at;
    }
}
