package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Buses implements Parcelable {

	@SerializedName("tahun")
	private int tahun;

	@SerializedName("no_tnkb")
	private String noTnkb;

	@SerializedName("tipe_id")
	private String tipeId;

	@SerializedName("kapasitas")
	private int kapasitas;

	@SerializedName("no_bus")
	private String noBus;

	protected Buses(Parcel in) {
		tahun = in.readInt();
		noTnkb = in.readString();
		tipeId = in.readString();
		kapasitas = in.readInt();
		noBus = in.readString();
	}

	public static final Creator<Buses> CREATOR = new Creator<Buses>() {
		@Override
		public Buses createFromParcel(Parcel in) {
			return new Buses(in);
		}

		@Override
		public Buses[] newArray(int size) {
			return new Buses[size];
		}
	};

	public void setTahun(int tahun){
		this.tahun = tahun;
	}

	public int getTahun(){
		return tahun;
	}

	public void setNoTnkb(String noTnkb){
		this.noTnkb = noTnkb;
	}

	public String getNoTnkb(){
		return noTnkb;
	}

	public void setTipeId(String tipeId){
		this.tipeId = tipeId;
	}

	public String getTipeId(){
		return tipeId;
	}

	public void setKapasitas(int kapasitas){
		this.kapasitas = kapasitas;
	}

	public int getKapasitas(){
		return kapasitas;
	}

	public void setNoBus(String noBus){
		this.noBus = noBus;
	}

	public String getNoBus(){
		return noBus;
	}

	@Override
 	public String toString(){
		return 
			"Buses{" + 
			"tahun = '" + tahun + '\'' + 
			",no_tnkb = '" + noTnkb + '\'' + 
			",tipe_id = '" + tipeId + '\'' + 
			",kapasitas = '" + kapasitas + '\'' + 
			",no_bus = '" + noBus + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(tahun);
		dest.writeString(noTnkb);
		dest.writeString(tipeId);
		dest.writeInt(kapasitas);
		dest.writeString(noBus);
	}
}