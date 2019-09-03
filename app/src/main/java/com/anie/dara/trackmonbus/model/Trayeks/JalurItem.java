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

	@SerializedName("nama_jalur")
	private String NamaJalur;

	@SerializedName("trayek_id")
	private String trayekId;

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

	public void setNamaJalur(String awal){
		this.NamaJalur = NamaJalur;
	}

	public String getNamaJalur(){
		return NamaJalur;
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
			"JalurItem{" + 
			"jalur_id = '" + jalurId + '\'' + 
			",halte = '" + halte + '\'' + 
			",urut = '" + urut + '\'' + 
			",NamaJalur = '" + NamaJalur + '\'' +
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
		dest.writeList(this.halte);
		dest.writeInt(this.urut);
		dest.writeString(this.NamaJalur);
		dest.writeString(this.trayekId);
	}

	public JalurItem() {
	}

	protected JalurItem(Parcel in) {
		this.jalurId = in.readString();
		this.halte = new ArrayList<HalteItem>();
		in.readList(this.halte, HalteItem.class.getClassLoader());
		this.urut = in.readInt();
		this.NamaJalur = in.readString();
		this.trayekId = in.readString();
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