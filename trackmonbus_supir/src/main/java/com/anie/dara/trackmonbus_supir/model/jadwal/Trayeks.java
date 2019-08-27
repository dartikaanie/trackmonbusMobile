package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trayeks implements Parcelable {

	@SerializedName("km_rit")
	private int kmRit;

	@SerializedName("trayek")
	private String trayek;

	@SerializedName("trayek_id")
	private String trayekId;

	protected Trayeks(Parcel in) {
		kmRit = in.readInt();
		trayek = in.readString();
		trayekId = in.readString();
	}

	public static final Creator<Trayeks> CREATOR = new Creator<Trayeks>() {
		@Override
		public Trayeks createFromParcel(Parcel in) {
			return new Trayeks(in);
		}

		@Override
		public Trayeks[] newArray(int size) {
			return new Trayeks[size];
		}
	};

	public void setKmRit(int kmRit){
		this.kmRit = kmRit;
	}

	public int getKmRit(){
		return kmRit;
	}

	public void setTrayek(String trayek){
		this.trayek = trayek;
	}

	public String getTrayek(){
		return trayek;
	}

	public void setTrayekId(String trayekId){
		this.trayekId = trayekId;
	}

	public String getTrayekId(){
		return trayekId;
	}

	@Override
 	public String toString(){
		return 
			"Trayeks{" + 
			"km_rit = '" + kmRit + '\'' + 
			",trayek = '" + trayek + '\'' + 
			",trayek_id = '" + trayekId + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(kmRit);
		dest.writeString(trayek);
		dest.writeString(trayekId);
	}
}