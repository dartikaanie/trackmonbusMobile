package com.anie.dara.trackmonbus_supir.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jadwal implements Parcelable {

    String tgl, no_bus, km_awal, km_akhir, keterangan, trayek, no_tnkb, kapasitas;
    String nama_supir,nama_pramugara, jam_awal, jam_akhir, jalur,shift;

    public String getShift() {
        return shift;
    }

    public String getTgl() {
        return tgl;
    }

    public String getNo_bus() {
        return no_bus;
    }

    public String getKm_awal() {
        return km_awal;
    }

    public String getKm_akhir() {
        return km_akhir;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getTrayek() {
        return trayek;
    }

    public String getNo_tnkb() {
        return no_tnkb;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public String getNama_supir() {
        return nama_supir;
    }

    public String getNama_pramugara() {
        return nama_pramugara;
    }

    public String getJam_awal() {
        return jam_awal;
    }

    public String getJam_akhir() {
        return jam_akhir;
    }

    public String getJalur() {
        return jalur;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tgl);
        dest.writeString(this.no_bus);
        dest.writeString(this.km_awal);
        dest.writeString(this.km_akhir);
        dest.writeString(this.keterangan);
        dest.writeString(this.trayek);
        dest.writeString(this.no_tnkb);
        dest.writeString(this.kapasitas);
        dest.writeString(this.nama_supir);
        dest.writeString(this.nama_pramugara);
        dest.writeString(this.jam_awal);
        dest.writeString(this.jam_akhir);
        dest.writeString(this.jalur);
    }

    public Jadwal() {
    }

    protected Jadwal(Parcel in) {
        this.tgl = in.readString();
        this.no_bus = in.readString();
        this.km_awal = in.readString();
        this.km_akhir = in.readString();
        this.keterangan = in.readString();
        this.trayek = in.readString();
        this.no_tnkb = in.readString();
        this.kapasitas = in.readString();
        this.nama_supir = in.readString();
        this.nama_pramugara = in.readString();
        this.jam_awal = in.readString();
        this.jam_akhir = in.readString();
        this.jalur = in.readString();
    }

    public static final Creator<Jadwal> CREATOR = new Creator<Jadwal>() {
        @Override
        public Jadwal createFromParcel(Parcel source) {
            return new Jadwal(source);
        }

        @Override
        public Jadwal[] newArray(int size) {
            return new Jadwal[size];
        }
    };
}
