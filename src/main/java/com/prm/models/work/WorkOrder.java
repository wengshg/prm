package com.prm.models.work;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.prm.models.listener.WorkOrderListener;

@Entity(name="workorder")
@EntityListeners({WorkOrderListener.class})
public class WorkOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long sid = 0L;
	private long fid;
	private long bid;
	private long lid;
	private long pid;
	private String code;
	private int sequence;
	private float quantity;
	private String unit;
	@Column(name="work_sdate")
	private Long workStartDate;
	@Column(name="work_edate")
	private Long workEndDate;
	private int status;
	@Column(name="owner_uid")
	private long ownerUid;
	@Column(name="weighing_uid")
	private Long weighingUid;
	@Column(name="operator_uid")
	private Long operatorUid;
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
	public Long getWorkStartDate() {
		return workStartDate;
	}
	public void setWorkStartDate(Long workStartDate) {
		this.workStartDate = workStartDate;
	}
	public Long getWorkEndDate() {
		return workEndDate;
	}
	public void setWorkEndDate(Long workEndDate) {
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
	public Long getWeighingUid() {
		return weighingUid;
	}
	public void setWeighingUid(Long weighingUid) {
		this.weighingUid = weighingUid;
	}
	public Long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

}
