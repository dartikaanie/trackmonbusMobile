package com.anie.dara.trackmonbus.model.distanceMatrix;

import com.google.gson.annotations.SerializedName;

public class ElementsItem{

	@SerializedName("duration")
	private Duration duration;

	@SerializedName("duration_in_traffic")
	private DurationInTraffic duration_in_traffic;

	@SerializedName("distance")
	private Distance distance;

	@SerializedName("status")
	private String status;

	public void setDuration(Duration duration){
		this.duration = duration;
	}

	public Duration getDuration(){
		return duration;
	}

	public void setDurationInTraffic(DurationInTraffic duration_in_traffic){
		this.duration_in_traffic = duration_in_traffic;
	}

	public DurationInTraffic getDurationInTraffic(){
		return duration_in_traffic;
	}

	public void setDistance(Distance distance){
		this.distance = distance;
	}

	public Distance getDistance(){
		return distance;
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
			"ElementsItem{" + 
			"duration = '" + duration + '\'' +
			"duration_in_traffic = '" + duration_in_traffic + '\'' +
			",distance = '" + distance + '\'' +
			",status = '" + status + '\'' + 
			"}";
		}
}