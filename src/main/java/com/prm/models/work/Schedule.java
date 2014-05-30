package com.prm.models.work;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
	@Column(name="schd_sdate")
	@Temporal(TemporalType.DATE)
	private Date schdStartDate;
	@Column(name="schd_edate")
	@Temporal(TemporalType.DATE)
	private Date schdEndDate;
	@Column(name="schd_time")
	@Temporal(TemporalType.TIME)
	private Date schdTime;
	@Column(name="schd_uid")
	private int schdUid;
	@Column(name="appr_time")
	@Temporal(TemporalType.TIME)
	private Date apprTime;
	@Column(name="appr_uid")
	private int apprUid;
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
	public Date getSchdStartDate() {
		return schdStartDate;
	}
	public void setSchdStartDate(Date schdStartDate) {
		this.schdStartDate = schdStartDate;
	}
	public Date getSchdEndDate() {
		return schdEndDate;
	}
	public void setSchdEndDate(Date schdEndDate) {
		this.schdEndDate = schdEndDate;
	}
	public Date getSchdTime() {
		return schdTime;
	}
	public void setSchdTime(Date schdTime) {
		this.schdTime = schdTime;
	}
	public int getSchdUid() {
		return schdUid;
	}
	public void setSchdUid(int schdUid) {
		this.schdUid = schdUid;
	}
	public Date getApprTime() {
		return apprTime;
	}
	public void setApprTime(Date apprTime) {
		this.apprTime = apprTime;
	}
	public int getApprUid() {
		return apprUid;
	}
	public void setApprUid(int apprUid) {
		this.apprUid = apprUid;
	}

}
