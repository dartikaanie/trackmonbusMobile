package com.anie.dara.trackmonbus.model;


import com.google.gson.annotations.SerializedName;

public class DataBus{

	@SerializedName("tahun")
	private String tahun;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("no_tnkb")
	private String noTnkb;

	@SerializedName("tipe_id")
	private String tipeId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("kapasitas")
	private int kapasitas;

	@SerializedName("tipe")
	private String tipe;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("no_bus")
	private String noBus;

	public void setTahun(String tahun){
		this.tahun = tahun;
	}

	public String getTahun(){
		return tahun;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setNoTnkb(String noTnkb){
		this.noTnkb = noTnkb;
	}

	public String getNoTnkb(){
		return noTnkb;
	}

	public void setTipeId(String tipeId){
		this.tipeId = tipeId;
	}

	public String getTipeId(){
		return tipeId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setKapasitas(int kapasitas){
		this.kapasitas = kapasitas;
	}

	public int getKapasitas(){
		return kapasitas;
	}

	public void setTipe(String tipe){
		this.tipe = tipe;
	}

	public String getTipe(){
		return tipe;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
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
			"DataBus{" + 
			"tahun = '" + tahun + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",no_tnkb = '" + noTnkb + '\'' + 
			",tipe_id = '" + tipeId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",kapasitas = '" + kapasitas + '\'' + 
			",tipe = '" + tipe + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",no_bus = '" + noBus + '\'' + 
			"}";
		}
}