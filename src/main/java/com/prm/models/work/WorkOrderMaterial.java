package com.prm.models.work;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="workorder_material")
public class WorkOrderMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long wid;
	private long mid;
	@Column(name="actl_total")
	private float actTotal;
	@Column(name="actl_quantity")
	private float actQuantity;
	private float quantity;
	@Column(name="container_qty")
	private int containerQty;
	private String unit;
	private int status;
	public long getWid() {
		return wid;
	}
	public void setWid(long wid) {
		this.wid = wid;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public int getContainerQty() {
		return containerQty;
	}
	public void setContainerQty(int containerQty) {
		this.containerQty = containerQty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getActTotal() {
		return actTotal;
	}
	public void setActTotal(float actTotal) {
		this.actTotal = actTotal;
	}
	public float getActQuantity() {
		return actQuantity;
	}
	public void setActQuantity(float actQuantity) {
		this.actQuantity = actQuantity;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}
