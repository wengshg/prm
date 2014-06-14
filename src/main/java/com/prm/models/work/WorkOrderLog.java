package com.prm.models.work;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="workorder_log")
public class WorkOrderLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long wid;
	private Long mid;
	private Long uid;
	private Integer sequence;
	private Integer status;
	@Column(name="created_time")
	private Long createdTime;
	@Column(name="created_uid")
	private Long createdUid;
	public Long getWid() {
		return wid;
	}
	public void setWid(Long wid) {
		this.wid = wid;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	public Long getCreatedUid() {
		return createdUid;
	}
	public void setCreatedUid(Long createdUid) {
		this.createdUid = createdUid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
