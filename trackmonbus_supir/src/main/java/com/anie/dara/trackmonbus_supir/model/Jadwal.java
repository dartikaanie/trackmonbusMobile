package com.anie.dara.trackmonbus_supir.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jadwal implements Parcelable {

    String tgl, no_bus, km_awal, km_akhir, keterangan, trayek, nama_halte, no_tnkb, kapasitas;
    String nama_supir, jam_awal, jam_akhir;

    protected Jadwal(Parcel in) {
        tgl = in.readString();
        no_bus = in.readString();
        km_awal = in.readString();
        km_akhir = in.readString();
        keterangan = in.readString();
        trayek = in.readString();
        nama_halte = in.readString();
        no_tnkb = in.readString();
        kapasitas = in.readString();
        nama_supir = in.readString();
        jam_awal = in.readString();
        jam_akhir = in.readString();
    }

    public static final Creator<Jadwal> CREATOR = new Creator<Jadwal>() {
        @Override
        public Jadwal createFromParcel(Parcel in) {
            return new Jadwal(in);
        }

        @Override
        public Jadwal[] newArray(int size) {
            return new Jadwal[size];
        }
    };

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

    public String getNama_halte() {
        return nama_halte;
    }

    public void setNama_halte(String nama_halte) {
        this.nama_halte = nama_halte;
    }

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

    public String getNama_supir() {
        return nama_supir;
    }

    public void setNama_supir(String nama_supir) {
        this.nama_supir = nama_supir;
    }

    public String getJam_awal() {
        return jam_awal;
    }

    public void setJam_awal(String jam_awal) {
        this.jam_awal = jam_awal;
    }

    public String getJam_akhir() {
        return jam_akhir;
    }

    public void setJam_akhir(String jam_akhir) {
        this.jam_akhir = jam_akhir;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tgl);
        parcel.writeString(no_bus);
        parcel.writeString(km_awal);
        parcel.writeString(km_akhir);
        parcel.writeString(keterangan);
        parcel.writeString(trayek);
        parcel.writeString(nama_halte);
        parcel.writeString(no_tnkb);
        parcel.writeString(kapasitas);
        parcel.writeString(nama_supir);
        parcel.writeString(jam_awal);
        parcel.writeString(jam_akhir);
    }
}
