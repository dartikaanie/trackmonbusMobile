package com.anie.dara.trackmonbus.model.Trayeks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JalurItem implements Parcelable {

	@SerializedName("jalur_id")
	private String jalurId;

	@SerializedName("halte")
	private List<HalteItem> halte;

	@SerializedName("urut")
	private int urut;

	@SerializedName("awal")
	private String awal;

	@SerializedName("trayek_id")
	private String trayekId;

	@SerializedName("akhir")
	private String akhir;

	public void setJalurId(String jalurId){
		this.jalurId = jalurId;
	}

	public String getJalurId(){
		return jalurId;
	}

	public void setHalte(List<HalteItem> halte){
		this.halte = halte;
	}

	public List<HalteItem> getHalte(){
		return halte;
	}

	public void setUrut(int urut){
		this.urut = urut;
	}

	public int getUrut(){
		return urut;
	}

	public void setAwal(String awal){
		this.awal = awal;
	}

	public String getAwal(){
		return awal;
	}

	public void setTrayekId(String trayekId){
		this.trayekId = trayekId;
	}

	public String getTrayekId(){
		return trayekId;
	}

	public void setAkhir(String akhir){
		this.akhir = akhir;
	}

	public String getAkhir(){
		return akhir;
	}

	@Override
 	public String toString(){
		return 
			"JalurItem{" + 
			"jalur_id = '" + jalurId + '\'' + 
			",halte = '" + halte + '\'' + 
			",urut = '" + urut + '\'' + 
			",awal = '" + awal + '\'' + 
			",trayek_id = '" + trayekId + '\'' + 
			",akhir = '" + akhir + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.jalurId);
		dest.writeList(this.halte);
		dest.writeInt(this.urut);
		dest.writeString(this.awal);
		dest.writeString(this.trayekId);
		dest.writeString(this.akhir);
	}

	public JalurItem() {
	}

	protected JalurItem(Parcel in) {
		this.jalurId = in.readString();
		this.halte = new ArrayList<HalteItem>();
		in.readList(this.halte, HalteItem.class.getClassLoader());
		this.urut = in.readInt();
		this.awal = in.readString();
		this.trayekId = in.readString();
		this.akhir = in.readString();
	}

	public static final Parcelable.Creator<JalurItem> CREATOR = new Parcelable.Creator<JalurItem>() {
		@Override
		public JalurItem createFromParcel(Parcel source) {
			return new JalurItem(source);
		}

		@Override
		public JalurItem[] newArray(int size) {
			return new JalurItem[size];
		}
	};
}