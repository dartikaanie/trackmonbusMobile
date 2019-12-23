package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Jadwal implements Parcelable {

	@SerializedName("jalur_awal_id")
	private String jalurAwalId;

		@SerializedName("km_akhir")
	private float kmAkhir;

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("buses")
	private Buses buses;

	@SerializedName("tgl")
	private String tgl;

	@SerializedName("detail_trayeks")
	private DetailTrayeks detailTrayeks;

	@SerializedName("km_awal")
	private float kmAwal;

	@SerializedName("trayek_id")
	private String trayekId;

	@SerializedName("no_bus")
	private String noBus;


	public void setJalurAwalId(String jalurAwalId){
		this.jalurAwalId = jalurAwalId;
	}

	public String getJalurAwalId(){
		return jalurAwalId;
	}

	public void setKmAkhir(float kmAkhir){
		this.kmAkhir = kmAkhir;
	}

	public float getKmAkhir(){
		return kmAkhir;
	}

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setBuses(Buses buses){
		this.buses = buses;
	}

	public Buses getBuses(){
		return buses;
	}

	public void setTgl(String tgl){
		this.tgl = tgl;
	}

	public String getTgl(){
		return tgl;
	}

	public void setDetailTrayeks(DetailTrayeks detailTrayeks){
		this.detailTrayeks = detailTrayeks;
	}

	public DetailTrayeks getDetailTrayeks(){
		return detailTrayeks;
	}

	public void setKmAwal(float kmAwal){
		this.kmAwal = kmAwal;
	}

	public float getKmAwal(){
		return kmAwal;
	}

	public void setTrayekId(String trayekId){
		this.trayekId = trayekId;
	}

	public String getTrayekId(){
		return trayekId;
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
			"Jadwal{" + 
			"jalur_awal_id = '" + jalurAwalId + '\'' + 
			",km_akhir = '" + kmAkhir + '\'' + 
			",keterangan = '" + keterangan + '\'' + 
			",buses = '" + buses + '\'' + 
			",tgl = '" + tgl + '\'' + 
			",detail_trayeks = '" + detailTrayeks + '\'' + 
			",km_awal = '" + kmAwal + '\'' + 
			",trayek_id = '" + trayekId + '\'' + 
			",no_bus = '" + noBus + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.jalurAwalId);
		dest.writeFloat(this.kmAkhir);
		dest.writeString(this.keterangan);
		dest.writeParcelable(this.buses, flags);
		dest.writeString(this.tgl);
		dest.writeParcelable(this.detailTrayeks, flags);
		dest.writeFloat(this.kmAwal);
		dest.writeString(this.trayekId);
		dest.writeString(this.noBus);
	}

	public Jadwal() {
	}

	protected Jadwal(Parcel in) {
		this.jalurAwalId = in.readString();
		this.kmAkhir = in.readFloat();
		this.keterangan = in.readString();
		this.buses = in.readParcelable(Buses.class.getClassLoader());
		this.tgl = in.readString();
		this.detailTrayeks = in.readParcelable(DetailTrayeks.class.getClassLoader());
		this.kmAwal = in.readFloat();
		this.trayekId = in.readString();
		this.noBus = in.readString();
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