package com.prm.models.work;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WorkOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long sid;
	private long fid;
	private long bid;
	private long lid;
	private long pid;
	private String code;
	private int sequence;
	private float quantity;
	private String unit;
	@Column(name="work_sdate")
	private Date workStartDate;
	@Column(name="work_edate")
	private Date workEndDate;
	private int status;
	@Column(name="owner_uid")
	private long ownerUid;
	@Column(name="weighing_uid")
	private long weighingUid;
	@Column(name="operator_uid")
	private long operatorUid;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
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
	public Date getWorkStartDate() {
		return workStartDate;
	}
	public void setWorkStartDate(Date workStartDate) {
		this.workStartDate = workStartDate;
	}
	public Date getWorkEndDate() {
		return workEndDate;
	}
	public void setWorkEndDate(Date workEndDate) {
		this.workEndDate = workEndDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getOwnerUid() {
		return ownerUid;
	}
	public void setOwnerUid(long ownerUid) {
		this.ownerUid = ownerUid;
	}
	public long getWeighingUid() {
		return weighingUid;
	}
	public void setWeighingUid(long weighingUid) {
		this.weighingUid = weighingUid;
	}
	public long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(long operatorUid) {
		this.operatorUid = operatorUid;
	}

}
