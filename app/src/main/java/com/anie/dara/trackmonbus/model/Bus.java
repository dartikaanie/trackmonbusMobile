package com.anie.dara.trackmonbus.model;

public class Bus {
    String no_bus;
    String no_tnkb;
    String tipe;
    int tahun;
    int kapasitas;

    public Bus(String no_bus, String no_tnkb, String tipe, int tahun, int kapasitas) {
        this.no_bus = no_bus;
        this.no_tnkb = no_tnkb;
        this.tipe = tipe;
        this.tahun = tahun;
        this.kapasitas = kapasitas;
    }

    public String getNo_bus() {
        return no_bus;
    }

    public void setNo_bus(String no_bus) {
        this.no_bus = no_bus;
    }

    public String getNo_tnkb() {
        return no_tnkb;
    }

    public void setNo_tnkb(String no_tnkb) {
        this.no_tnkb = no_tnkb;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
}
