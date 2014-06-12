package com.prm.models.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BomItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long bid;
	private long mid;
	private long pid;
	private Float tolerance;
	private float quantity;
	private String unit;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public long getBid() {
		return bid;
	}
	public void setBid(long bid) {
		this.bid = bid;
	}
	public Float getTolerance() {
		return tolerance;
	}
	public void setTolerance(Float tolerance) {
		this.tolerance = tolerance;
	}

}
