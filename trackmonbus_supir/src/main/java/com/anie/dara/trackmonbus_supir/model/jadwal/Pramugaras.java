package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Pramugaras implements Parcelable {

	@SerializedName("user_id")
	private String pramugaraNik;

	@SerializedName("name")
	private String namaPramugara;

	protected Pramugaras(Parcel in) {
		pramugaraNik = in.readString();
		namaPramugara = in.readString();
	}

	public static final Creator<Pramugaras> CREATOR = new Creator<Pramugaras>() {
		@Override
		public Pramugaras createFromParcel(Parcel in) {
			return new Pramugaras(in);
		}

		@Override
		public Pramugaras[] newArray(int size) {
			return new Pramugaras[size];
		}
	};

	public void setPramugaraNik(String pramugaraNik){
		this.pramugaraNik = pramugaraNik;
	}

	public String getPramugaraNik(){
		return pramugaraNik;
	}

	public void setNamaPramugara(String namaPramugara){
		this.namaPramugara = namaPramugara;
	}

	public String getNamaPramugara(){
		return namaPramugara;
	}

	@Override
 	public String toString(){
		return 
			"Pramugaras{" + 
			"user_id = '" + pramugaraNik + '\'' +
			",nama_pramugara = '" + namaPramugara + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(pramugaraNik);
		dest.writeString(namaPramugara);
	}
}