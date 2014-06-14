package com.prm.models.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProcessFlowItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long fid;
	private Long bid;
	private Long lid;
	private Long pid;
	private Long mid;
	private Integer sequence;
	private Integer Integererval;
	@Column(name="eqpt_type")
	private String eqptType;
	@Column(name="gate_type")
	private String gateType;
	private Integer enable;
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
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Integer getIntegererval() {
		return Integererval;
	}
	public void setIntegererval(Integer Integererval) {
		this.Integererval = Integererval;
	}
	public String getEqptType() {
		return eqptType;
	}
	public void setEqptType(String eqptType) {
		this.eqptType = eqptType;
	}
	public String getGateType() {
		return gateType;
	}
	public void setGateType(String gateType) {
		this.gateType = gateType;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
