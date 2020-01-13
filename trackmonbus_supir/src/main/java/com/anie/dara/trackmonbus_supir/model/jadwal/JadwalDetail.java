package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class JadwalDetail implements Parcelable {

	@SerializedName("user_id_supir")
	private String userIdSupir;

	@SerializedName("jadwal")
	private Jadwal jadwal;

	@SerializedName("user_id_pramugara")
	private String pramugaraNik;

	@SerializedName("shift_id")
	private String shiftId;

	@SerializedName("pramugaras")
	private Pramugaras pramugaras;

	@SerializedName("tgl")
	private String tgl;

	@SerializedName("shifts")
	private Shifts shifts;

	@SerializedName("supirs")
	private Users users;

	@SerializedName("no_bus")
	private String noBus;

	public void setPramugaraNik(String pramugaraNik){
		this.pramugaraNik = pramugaraNik;
	}

	public String getPramugaraNik(){
		return pramugaraNik;
	}

	public void setUserIdSupir(String userIdSupir){
		this.userIdSupir = userIdSupir;
	}

	public String getUserIdSupir(){
		return userIdSupir;
	}

	public void setJadwal(Jadwal jadwal){
		this.jadwal = jadwal;
	}

	public Jadwal getJadwal(){
		return jadwal;
	}

	public void setShiftId(String shiftId){
		this.shiftId = shiftId;
	}

	public String getShiftId(){
		return shiftId;
	}

	public void setPramugaras(Pramugaras pramugaras){
		this.pramugaras = pramugaras;
	}

	public Pramugaras getPramugaras(){
		return pramugaras;
	}

	public void setTgl(String tgl){
		this.tgl = tgl;
	}

	public String getTgl(){
		return tgl;
	}

	public void setShifts(Shifts shifts){
		this.shifts = shifts;
	}

	public Shifts getShifts(){
		return shifts;
	}

	public void setUsers(Users users){
		this.users = users;
	}

	public Users getUsers(){
		return users;
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
			"JadwalDetail{" + 
			"pramugara_nik = '" + pramugaraNik + '\'' + 
			",user_id_supir = '" + userIdSupir + '\'' + 
			",jadwal = '" + jadwal + '\'' + 
			",shift_id = '" + shiftId + '\'' + 
			",pramugaras = '" + pramugaras + '\'' + 
			",tgl = '" + tgl + '\'' + 
			",shifts = '" + shifts + '\'' + 
			",users = '" + users + '\'' + 
			",no_bus = '" + noBus + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.pramugaraNik);
		dest.writeString(this.userIdSupir);
		dest.writeParcelable(this.jadwal, flags);
		dest.writeString(this.shiftId);
		dest.writeParcelable(this.pramugaras, flags);
		dest.writeString(this.tgl);
		dest.writeParcelable(this.shifts, flags);
		dest.writeParcelable(this.users, flags);
		dest.writeString(this.noBus);
	}

	public JadwalDetail() {
	}

	protected JadwalDetail(Parcel in) {
		this.pramugaraNik = in.readString();
		this.userIdSupir = in.readString();
		this.jadwal = in.readParcelable(Jadwal.class.getClassLoader());
		this.shiftId = in.readString();
		this.pramugaras = in.readParcelable(Pramugaras.class.getClassLoader());
		this.tgl = in.readString();
		this.shifts = in.readParcelable(Shifts.class.getClassLoader());
		this.users = in.readParcelable(Users.class.getClassLoader());
		this.noBus = in.readString();
	}

	public static final Parcelable.Creator<JadwalDetail> CREATOR = new Parcelable.Creator<JadwalDetail>() {
		@Override
		public JadwalDetail createFromParcel(Parcel source) {
			return new JadwalDetail(source);
		}

		@Override
		public JadwalDetail[] newArray(int size) {
			return new JadwalDetail[size];
		}
	};
}