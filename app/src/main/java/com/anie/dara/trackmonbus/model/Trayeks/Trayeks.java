package com.anie.dara.trackmonbus.model.Trayeks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Trayeks implements Parcelable {

	@SerializedName("jalur")
	private List<JalurItem> jalur;

	@SerializedName("km_rit")
	private Float kmRit;

	@SerializedName("trayek")
	private String trayek;

	@SerializedName("trayek_id")
	private int trayekId;

	public void setJalur(List<JalurItem> jalur) {
		this.jalur = jalur;
	}

	public List<JalurItem> getJalur() {
		return jalur;
	}

	public void setKmRit(Float kmRit) {
		this.kmRit = kmRit;
	}

	public Float getKmRit() {
		return kmRit;
	}

	public void setTrayek(String trayek) {
		this.trayek = trayek;
	}

	public String getTrayek() {
		return trayek;
	}

	public void setTrayekId(int trayekId) {
		this.trayekId = trayekId;
	}

	public int getTrayekId() {
		return trayekId;
	}


	@Override
	public String toString() {
		return
				"Trayeks{" +
						"jalur = '" + jalur + '\'' +
						",km_rit = '" + kmRit + '\'' +
						",trayek = '" + trayek + '\'' +
						",trayek_id = '" + trayekId + '\'' +
						"}";
	}


	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.jalur);
		dest.writeValue(this.kmRit);
		dest.writeString(this.trayek);
		dest.writeInt(this.trayekId);
	}

	public Trayeks() {
	}

	protected Trayeks(Parcel in) {
		this.jalur = new ArrayList<JalurItem>();
		in.readList(this.jalur, JalurItem.class.getClassLoader());
		this.kmRit = (Float) in.readValue(Float.class.getClassLoader());
		this.trayek = in.readString();
		this.trayekId = in.readInt();
	}

	public static final Creator<Trayeks> CREATOR = new Creator<Trayeks>() {
		@Override
		public Trayeks createFromParcel(Parcel source) {
			return new Trayeks(source);
		}

		@Override
		public Trayeks[] newArray(int size) {
			return new Trayeks[size];
		}
	};
}