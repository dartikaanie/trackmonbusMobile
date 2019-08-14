package com.anie.dara.trackmonbus_supir.mapDirection;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class mapDirection {

	@SerializedName("routes")
	private List<RoutesItem> routes;


	@SerializedName("status")
	private String status;

	public void setRoutes(List<RoutesItem> routes){
		this.routes = routes;
	}

	public List<RoutesItem> getRoutes(){
		return routes;
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
			"Response{" + 
			"routes = '" + routes + '\'' + 
			",status = '" + status + '\'' +
			"}";
		}
}