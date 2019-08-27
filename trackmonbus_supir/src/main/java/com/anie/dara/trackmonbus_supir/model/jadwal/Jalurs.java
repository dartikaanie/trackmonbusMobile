package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Jalurs implements Parcelable {

	@SerializedName("jalur_id")
	private String jalurId;

	@SerializedName("nama_jalur")
	private String namaJalur;

	protected Jalurs(Parcel in) {
		jalurId = in.readString();
		namaJalur = in.readString();
	}

	public static final Creator<Jalurs> CREATOR = new Creator<Jalurs>() {
		@Override
		public Jalurs createFromParcel(Parcel in) {
			return new Jalurs(in);
		}

		@Override
		public Jalurs[] newArray(int size) {
			return new Jalurs[size];
		}
	};

	public void setJalurId(String jalurId){
		this.jalurId = jalurId;
	}

	public String getJalurId(){
		return jalurId;
	}

	public void setNamaJalur(String namaJalur){
		this.namaJalur = namaJalur;
	}

	public String getNamaJalur(){
		return namaJalur;
	}

	@Override
 	public String toString(){
		return 
			"Jalurs{" + 
			"jalur_id = '" + jalurId + '\'' + 
			",nama_jalur = '" + namaJalur + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jalurId);
		dest.writeString(namaJalur);
	}
}