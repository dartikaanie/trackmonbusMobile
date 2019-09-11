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
	private Double lng;

	@SerializedName("urut")
	private int urut;

	@SerializedName("lat")
	private Double lat;

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

	public void setLng(Double lng){
		this.lng = lng;
	}

	public Double getLng(){
		return lng;
	}

	public void setUrut(int urut){
		this.urut = urut;
	}

	public int getUrut(){
		return urut;
	}

	public void setLat(Double lat){
		this.lat = lat;
	}

	public Double getLat(){
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
		dest.writeDouble(this.lng);
		dest.writeInt(this.urut);
		dest.writeDouble(this.lat);
	}

	public HalteItem() {
	}

	protected HalteItem(Parcel in) {
		this.halteId = in.readString();
		this.nama = in.readString();
		this.lng = in.readDouble();
		this.urut = in.readInt();
		this.lat = in.readDouble();
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