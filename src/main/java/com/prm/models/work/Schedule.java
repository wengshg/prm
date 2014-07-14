package com.prm.models.work;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
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
	private Long id;
	@JoinTable(name="process_flow", joinColumns = @JoinColumn(name="fid",referencedColumnName="id"))
	private Long fid;
	private Long bid;
	private Long lid;
	private Long pid;
	private String code;
	private Float quantity;
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFid() {
		return fid;
	}
	public void setFid(Long fid) {
		this.fid = fid;
	}
	public Long getBid() {
		return bid;
	}
	public void setBid(Long bid) {
		this.bid = bid;
	}
	public Long getLid() {
		return lid;
	}
	public void setLid(Long lid) {
		this.lid = lid;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Float getQuantity() {
		return quantity;
	}
	public void setQuantity(Float quantity) {
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
