package com.prm.models.work;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="workorder_container")
public class WorkOrderContainer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long wid;
	private long mid;
	private long fid;
	private long bid;
	private long lid;
	private long pid;
	private long eid;
	private long gid;
	private float total;
	private float quantity;
	@Column(name="container_qty")
	private float containerQty;
	private String code;
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
	public long getFid() {
		return fid;
	}
	public void setFid(long fid) {
		this.fid = fid;
	}
	public long getBid() {
		return bid;
	}
	public void setBid(long bid) {
		this.bid = bid;
	}
	public long getLid() {
		return lid;
	}
	public void setLid(long lid) {
		this.lid = lid;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public long getEid() {
		return eid;
	}
	public void setEid(long eid) {
		this.eid = eid;
	}
	public long getGid() {
		return gid;
	}
	public void setGid(long gid) {
		this.gid = gid;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public float getContainerQty() {
		return containerQty;
	}
	public void setContainerQty(float containerQty) {
		this.containerQty = containerQty;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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

}
