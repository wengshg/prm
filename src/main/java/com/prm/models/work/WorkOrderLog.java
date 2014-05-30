package com.prm.models.work;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WorkOrderLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long wid;
	private long mid;
	private long uid;
	private int sequence;
	private int status;
	@Column(name="created_time")
	private Date createdTime;
	@Column(name="created_uid")
	private long createdUid;
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
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public long getCreatedUid() {
		return createdUid;
	}
	public void setCreatedUid(long createdUid) {
		this.createdUid = createdUid;
	}

}
