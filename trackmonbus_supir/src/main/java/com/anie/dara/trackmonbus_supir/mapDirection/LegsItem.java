package com.anie.dara.trackmonbus_supir.mapDirection;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LegsItem {

	@SerializedName("duration")
	private Duration duration;


	@SerializedName("distance")
	private Distance distance;

	@SerializedName("start_address")
	private String startAddress;


	@SerializedName("end_address")
	private String endAddress;

	@SerializedName("via_waypoint")
	private List<Object> viaWaypoint;


	@SerializedName("traffic_speed_entry")
	private List<Object> trafficSpeedEntry;

	public void setDuration(Duration duration){
		this.duration = duration;
	}

	public Duration getDuration(){
		return duration;
	}


	public void setDistance(Distance distance){
		this.distance = distance;
	}

	public Distance getDistance(){
		return distance;
	}

	public void setStartAddress(String startAddress){
		this.startAddress = startAddress;
	}

	public String getStartAddress(){
		return startAddress;
	}


	public void setEndAddress(String endAddress){
		this.endAddress = endAddress;
	}

	public String getEndAddress(){
		return endAddress;
	}

	public void setViaWaypoint(List<Object> viaWaypoint){
		this.viaWaypoint = viaWaypoint;
	}

	public List<Object> getViaWaypoint(){
		return viaWaypoint;
	}


	public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry){
		this.trafficSpeedEntry = trafficSpeedEntry;
	}

	public List<Object> getTrafficSpeedEntry(){
		return trafficSpeedEntry;
	}

	@Override
 	public String toString(){
		return 
			"LegsItem{" + 
			"duration = '" + duration + '\'' + 
			",distance = '" + distance + '\'' +
			",start_address = '" + startAddress + '\'' + 
			",end_address = '" + endAddress + '\'' +
			",via_waypoint = '" + viaWaypoint + '\'' + 
			",traffic_speed_entry = '" + trafficSpeedEntry + '\'' +
			"}";
		}
}