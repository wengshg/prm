package com.prm.models.work;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private long fid;
	private long bid;
	private long lid;
	private long pid;
	private String code;
	private float quantity;
	private String unit;
	@Column(name="schd_sdate", nullable=false)
	private Long schdStartDate;
	@Column(name="schd_edate", nullable=false)
	private Long schdEndDate;
	@Column(name="schd_time")
	private Long schdTime = Calendar.getInstance().getTimeInMillis();
	@Column(name="schd_uid")
	private Long schdUid;
	@Column(name="appr_time")
	private Long apprTime;
	@Column(name="appr_uid")
	private Long apprUid;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public Long getSchdStartDate() {
		return schdStartDate;
	}
	public void setSchdStartDate(Long schdStartDate) {
		this.schdStartDate = schdStartDate;
	}
	public Long getSchdEndDate() {
		return schdEndDate;
	}
	public void setSchdEndDate(Long schdEndDate) {
		this.schdEndDate = schdEndDate;
	}
	public Long getSchdTime() {
		return schdTime;
	}
	public void setSchdTime(Long schdTime) {
		this.schdTime = schdTime;
	}
	public Long getSchdUid() {
		return schdUid;
	}
	public void setSchdUid(Long schdUid) {
		this.schdUid = schdUid;
	}
	public Long getApprTime() {
		return apprTime;
	}
	public void setApprTime(Long apprTime) {
		this.apprTime = apprTime;
	}
	public Long getApprUid() {
		return apprUid;
	}
	public void setApprUid(Long apprUid) {
		this.apprUid = apprUid;
	}

}
