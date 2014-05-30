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
	private long fid;
	private long bid;
	private long lid;
	private long pid;
	private long mid;
	private int sequence;
	private int interval;
	@Column(name="eqpt_type")
	private String eqptType;
	@Column(name="gate_type")
	private String gateType;
	private int enable;
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
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
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
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}

}
