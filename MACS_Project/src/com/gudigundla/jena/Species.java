package com.gudigundla.jena;

import java.util.Calendar;

public class Species {
	
	private String id;
	private String sname;
	private float minLatitude;
	private float minLongitude;
	private float maxLatitude;
	private float maxLongitude;
	private Calendar from;
	private Calendar to;
	
	public Calendar getFrom() {
		return from;
	}
	public void setFrom(Calendar from) {
		this.from = from;
	}
	public Calendar getTo() {
		return to;
	}
	public void setTo(Calendar to) {
		this.to = to;
	}
	public float getMinLatitude() {
		return minLatitude;
	}
	public void setMinLatitude(float minLatitude) {
		this.minLatitude = minLatitude;
	}
	public float getMinLongitude() {
		return minLongitude;
	}
	public void setMinLongitude(float minLongitude) {
		this.minLongitude = minLongitude;
	}
	public float getMaxLatitude() {
		return maxLatitude;
	}
	public void setMaxLatitude(float maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
	public float getMaxLongitude() {
		return maxLongitude;
	}
	public void setMaxLongitude(float maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
		
	public Species(String sname, String id) {
		this.sname = sname;
		this.id = id;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sname;
	}
}
