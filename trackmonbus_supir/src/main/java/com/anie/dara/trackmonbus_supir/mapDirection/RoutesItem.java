package com.anie.dara.trackmonbus_supir.mapDirection;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoutesItem {

	@SerializedName("summary")
	private String summary;

	@SerializedName("copyrights")
	private String copyrights;

	@SerializedName("legs")
	private List<LegsItem> legs;

	@SerializedName("warnings")
	private List<Object> warnings;

	@SerializedName("waypoint_order")
	private List<Object> waypointOrder;

	public void setSummary(String summary){
		this.summary = summary;
	}

	public String getSummary(){
		return summary;
	}

	public void setCopyrights(String copyrights){
		this.copyrights = copyrights;
	}

	public String getCopyrights(){
		return copyrights;
	}

	public void setLegs(List<LegsItem> legs){
		this.legs = legs;
	}

	public List<LegsItem> getLegs(){
		return legs;
	}

	public void setWarnings(List<Object> warnings){
		this.warnings = warnings;
	}

	public List<Object> getWarnings(){
		return warnings;
	}

	public void setWaypointOrder(List<Object> waypointOrder){
		this.waypointOrder = waypointOrder;
	}

	public List<Object> getWaypointOrder(){
		return waypointOrder;
	}

	@Override
 	public String toString(){
		return 
			"RoutesItem{" + 
			"summary = '" + summary + '\'' + 
			",copyrights = '" + copyrights + '\'' + 
			",legs = '" + legs + '\'' + 
			",warnings = '" + warnings + '\'' + 
			",waypoint_order = '" + waypointOrder + '\'' +
			"}";
		}
}