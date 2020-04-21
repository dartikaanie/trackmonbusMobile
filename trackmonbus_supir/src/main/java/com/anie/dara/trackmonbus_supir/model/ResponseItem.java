package com.anie.dara.trackmonbus_supir.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseItem{

	@SerializedName("device_id")
	private int deviceId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("log")
	private List<Object> log;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setDeviceId(int deviceId){
		this.deviceId = deviceId;
	}

	public int getDeviceId(){
		return deviceId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setLog(List<Object> log){
		this.log = log;
	}

	public List<Object> getLog(){
		return log;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseItem{" + 
			"device_id = '" + deviceId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",log = '" + log + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",phone_number = '" + phoneNumber + '\'' + 
			",id = '" + id + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}