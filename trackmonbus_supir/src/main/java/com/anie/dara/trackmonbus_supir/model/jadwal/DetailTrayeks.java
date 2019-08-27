package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class DetailTrayeks implements Parcelable {

	@SerializedName("jalur_id")
	private String jalurId;

	@SerializedName("urut")
	private int urut;

	@SerializedName("jalurs")
	private Jalurs jalurs;

	@SerializedName("trayeks")
	private Trayeks trayeks;

	@SerializedName("trayek_id")
	private String trayekId;


	public void setJalurId(String jalurId){
		this.jalurId = jalurId;
	}

	public String getJalurId(){
		return jalurId;
	}

	public void setUrut(int urut){
		this.urut = urut;
	}

	public int getUrut(){
		return urut;
	}

	public void setJalurs(Jalurs jalurs){
		this.jalurs = jalurs;
	}

	public Jalurs getJalurs(){
		return jalurs;
	}

	public void setTrayeks(Trayeks trayeks){
		this.trayeks = trayeks;
	}

	public Trayeks getTrayeks(){
		return trayeks;
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
			"DetailTrayeks{" + 
			"jalur_id = '" + jalurId + '\'' + 
			",urut = '" + urut + '\'' + 
			",jalurs = '" + jalurs + '\'' + 
			",trayeks = '" + trayeks + '\'' + 
			",trayek_id = '" + trayekId + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.jalurId);
		dest.writeInt(this.urut);
		dest.writeParcelable(this.jalurs, flags);
		dest.writeParcelable(this.trayeks, flags);
		dest.writeString(this.trayekId);
	}

	public DetailTrayeks() {
	}

	protected DetailTrayeks(Parcel in) {
		this.jalurId = in.readString();
		this.urut = in.readInt();
		this.jalurs = in.readParcelable(Jalurs.class.getClassLoader());
		this.trayeks = in.readParcelable(Trayeks.class.getClassLoader());
		this.trayekId = in.readString();
	}

	public static final Creator<DetailTrayeks> CREATOR = new Creator<DetailTrayeks>() {
		@Override
		public DetailTrayeks createFromParcel(Parcel source) {
			return new DetailTrayeks(source);
		}

		@Override
		public DetailTrayeks[] newArray(int size) {
			return new DetailTrayeks[size];
		}
	};
}