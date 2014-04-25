package edu.auburn.csse.comp3710.group10.auexplore;

import com.google.android.gms.maps.model.LatLng;

public class AULocation {
	
	private LatLng latLng;
	private String name;
	private String description;
	private int pointVal;
	private boolean isFound;
	private int id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isFound() {
		return isFound;
	}
	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}
	public LatLng getLatLng() {
		return latLng;
	}
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPointVal() {
		return pointVal;
	}
	public void setPointVal(int pointVal) {
		this.pointVal = pointVal;
	}
	
	
	
}
