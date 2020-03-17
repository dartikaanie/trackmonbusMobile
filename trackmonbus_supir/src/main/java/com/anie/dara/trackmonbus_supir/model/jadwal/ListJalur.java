package com.anie.dara.trackmonbus_supir.model.jadwal;

import com.google.gson.annotations.SerializedName;


public class ListJalur {

	@SerializedName("jalur_id")
	private String jalurId;

	public void setJalurId(String jalurId){
		this.jalurId = jalurId;
	}

	public String getJalurId(){
		return jalurId;
	}

	@Override
 	public String toString(){
		return 
			"JalurItem{" + 
			"jalur_id = '" + jalurId + '\'' + 
			"}";
		}
}