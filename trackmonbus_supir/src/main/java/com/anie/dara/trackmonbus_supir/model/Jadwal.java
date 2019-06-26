package com.anie.dara.trackmonbus_supir.model;

public class Jadwal {

    String tgl, no_bus, km_awal, km_akhir, keterangan, trayek, nama, no_tnkb, kapasitas;
    String nama_halte;

    public String getNo_tnkb() {
        return no_tnkb;
    }

    public void setNo_tnkb(String no_tnkb) {
        this.no_tnkb = no_tnkb;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getNama_halte() {
        return nama_halte;
    }

    public void setNama_halte(String nama) {
        this.nama_halte = nama;
    }

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

    public String getKm_awal() {
        return km_awal;
    }

    public void setKm_awal(String km_awal) {
        this.km_awal = km_awal;
    }

    public String getKm_akhir() {
        return km_akhir;
    }

    public void setKm_akhir(String km_akhir) {
        this.km_akhir = km_akhir;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTrayek() {
        return trayek;
    }

    public void setTrayek(String trayek) {
        this.trayek = trayek;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
