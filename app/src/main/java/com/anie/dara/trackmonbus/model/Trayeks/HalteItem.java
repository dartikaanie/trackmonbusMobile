package com.anie.dara.trackmonbus.model.Trayeks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HalteItem implements Parcelable {

	@SerializedName("halte_id")
	private String halteId;

	@SerializedName("nama")
	private String nama;

	@SerializedName("lng")
	private String lng;

	@SerializedName("urut")
	private int urut;

	@SerializedName("lat")
	private String lat;

	public void setHalteId(String halteId){
		this.halteId = halteId;
	}

	public String getHalteId(){
		return halteId;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setLng(String lng){
		this.lng = lng;
	}

	public String getLng(){
		return lng;
	}

	public void setUrut(int urut){
		this.urut = urut;
	}

	public int getUrut(){
		return urut;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"HalteItem{" + 
			"halte_id = '" + halteId + '\'' + 
			",nama = '" + nama + '\'' + 
			",lng = '" + lng + '\'' + 
			",urut = '" + urut + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.halteId);
		dest.writeString(this.nama);
		dest.writeString(this.lng);
		dest.writeInt(this.urut);
		dest.writeString(this.lat);
	}

	public HalteItem() {
	}

	protected HalteItem(Parcel in) {
		this.halteId = in.readString();
		this.nama = in.readString();
		this.lng = in.readString();
		this.urut = in.readInt();
		this.lat = in.readString();
	}

	public static final Parcelable.Creator<HalteItem> CREATOR = new Parcelable.Creator<HalteItem>() {
		@Override
		public HalteItem createFromParcel(Parcel source) {
			return new HalteItem(source);
		}

		@Override
		public HalteItem[] newArray(int size) {
			return new HalteItem[size];
		}
	};
}