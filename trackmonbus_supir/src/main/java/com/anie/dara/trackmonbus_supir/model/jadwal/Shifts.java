package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Shifts implements Parcelable {

	@SerializedName("shift_id")
	private String shiftId;

	@SerializedName("shift")
	private String shift;

	@SerializedName("jam_akhir")
	private String jamAkhir;

	@SerializedName("jam_awal")
	private String jamAwal;

	protected Shifts(Parcel in) {
		shiftId = in.readString();
		shift = in.readString();
		jamAkhir = in.readString();
		jamAwal = in.readString();
	}

	public static final Creator<Shifts> CREATOR = new Creator<Shifts>() {
		@Override
		public Shifts createFromParcel(Parcel in) {
			return new Shifts(in);
		}

		@Override
		public Shifts[] newArray(int size) {
			return new Shifts[size];
		}
	};

	public void setShiftId(String shiftId){
		this.shiftId = shiftId;
	}

	public String getShiftId(){
		return shiftId;
	}

	public void setShift(String shift){
		this.shift = shift;
	}

	public String getShift(){
		return shift;
	}

	public void setJamAkhir(String jamAkhir){
		this.jamAkhir = jamAkhir;
	}

	public String getJamAkhir(){
		return jamAkhir;
	}

	public void setJamAwal(String jamAwal){
		this.jamAwal = jamAwal;
	}

	public String getJamAwal(){
		return jamAwal;
	}

	@Override
 	public String toString(){
		return 
			"Shifts{" + 
			"shift_id = '" + shiftId + '\'' + 
			",shift = '" + shift + '\'' + 
			",jam_akhir = '" + jamAkhir + '\'' + 
			",jam_awal = '" + jamAwal + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(shiftId);
		dest.writeString(shift);
		dest.writeString(jamAkhir);
		dest.writeString(jamAwal);
	}
}