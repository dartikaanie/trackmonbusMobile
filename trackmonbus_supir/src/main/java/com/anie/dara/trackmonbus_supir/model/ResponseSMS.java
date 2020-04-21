package com.anie.dara.trackmonbus_supir.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseSMS{

	@SerializedName("response")
	private List<ResponseItem> response;

	@SerializedName("status")
	private int status;

	public void setResponse(List<ResponseItem> response){
		this.response = response;
	}

	public List<ResponseItem> getResponse(){
		return response;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseSMS{" + 
			"response = '" + response + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}