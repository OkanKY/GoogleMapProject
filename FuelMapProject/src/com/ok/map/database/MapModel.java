package com.ok.map.database;



public class MapModel {	
	private long id = -1;
	private Double startLatitude;
	private Double startLongitude;
	private Double targetLatitude;
	private Double targetLongitude;
	private String name;
	private Double cost;
	private String distance;
	private String duration;
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}


	public Double getStartLatitude() {
		return startLatitude;
	}
	public void setStartLatitude(Double startLatitude) {
		this.startLatitude = startLatitude;
	}
	public Double getStartLongitude() {
		return startLongitude;
	}
	public void setStartLongitude(Double startLongitude) {
		this.startLongitude = startLongitude;
	}
	public Double getTargetLatitude() {
		return targetLatitude;
	}
	public void setTargetLatitude(Double targetLatitude) {
		this.targetLatitude = targetLatitude;
	}
	public Double getTargetLongitude() {
		return targetLongitude;
	}
	public void setTargetLongitude(Double targetLongitude) {
		this.targetLongitude = targetLongitude;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		        return "Kayýt isimi:  "+this.name +"  Mesafe:  "+this.distance;
	 }
	

	
}
