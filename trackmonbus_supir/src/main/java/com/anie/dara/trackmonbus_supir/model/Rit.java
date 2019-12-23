package com.anie.dara.trackmonbus_supir.model;

import com.anie.dara.trackmonbus_supir.model.jadwal.DetailTrayeks;
import com.google.gson.annotations.SerializedName;

public class Rit{

	@SerializedName("jalur_id")
	private String jalurId;

	@SerializedName("waktu_berangkat")
	private String waktuBerangkat;

	@SerializedName("tgl")
	private String tgl;

	@SerializedName("detail_trayeks")
	private DetailTrayeks detailTrayeks;

	@SerializedName("waktu_datang")
	private String waktuDatang;

	@SerializedName("trayek_id")
	private String trayekId;

	@SerializedName("rits_id")
	private String ritsId;

	@SerializedName("no_bus")
	private String noBus;

	public void setJalurId(String jalurId){
		this.jalurId = jalurId;
	}

	public String getJalurId(){
		return jalurId;
	}

	public void setWaktuBerangkat(String waktuBerangkat){
		this.waktuBerangkat = waktuBerangkat;
	}

	public String getWaktuBerangkat(){
		return waktuBerangkat;
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

	public void setWaktuDatang(String waktuDatang){
		this.waktuDatang = waktuDatang;
	}

	public String getWaktuDatang(){
		return waktuDatang;
	}

	public void setTrayekId(String trayekId){
		this.trayekId = trayekId;
	}

	public String getTrayekId(){
		return trayekId;
	}

	public void setRitsId(String ritsId){
		this.ritsId = ritsId;
	}

	public String getRitsId(){
		return ritsId;
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
			"Rit{" + 
			"jalur_id = '" + jalurId + '\'' + 
			",waktu_berangkat = '" + waktuBerangkat + '\'' + 
			",tgl = '" + tgl + '\'' + 
			",detail_trayeks = '" + detailTrayeks + '\'' + 
			",waktu_datang = '" + waktuDatang + '\'' + 
			",trayek_id = '" + trayekId + '\'' + 
			",rits_id = '" + ritsId + '\'' + 
			",no_bus = '" + noBus + '\'' + 
			"}";
		}
}