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
	private Long id;
	private Long sid = 0L;
	private Long fid;
	private Long bid;
	private Long lid;
	private Long pid;
	private String code;
	private Integer sequence;
	private Float quantity = 0F;
	private String unit;
	@Column(name="work_sdate")
	private Long workStartDate;
	@Column(name="work_edate")
	private Long workEndDate;
	private Integer status = 0;
	@Column(name="owner_uid")
	private Long ownerUid;
	@Column(name="weighing_uid")
	private Long weighingUid;
	@Column(name="operator_uid")
	private Long operatorUid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
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
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getOwnerUid() {
		return ownerUid;
	}
	public void setOwnerUid(Long ownerUid) {
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
