package com.anie.dara.trackmonbus_supir.model.jadwal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Users implements Parcelable {

	@SerializedName("user_id")
	private int userId;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	protected Users(Parcel in) {
		userId = in.readInt();
		name = in.readString();
		email = in.readString();
	}

	public static final Creator<Users> CREATOR = new Creator<Users>() {
		@Override
		public Users createFromParcel(Parcel in) {
			return new Users(in);
		}

		@Override
		public Users[] newArray(int size) {
			return new Users[size];
		}
	};

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"Users{" + 
			"user_id = '" + userId + '\'' + 
			",name = '" + name + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(userId);
		dest.writeString(name);
		dest.writeString(email);
	}
}