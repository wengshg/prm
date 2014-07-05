package com.prm.models.work;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="workorder_material")
public class WorkOrderMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long sid;
	private Long wid;
	private Long mid;
	private Long lid;
	private Long pid;
	private Integer replenish;
	@Column(name="actl_total")
	private Float actTotal = 0F;
	@Column(name="actl_quantity")
	private Float actQuantity = 0F;
	private Float quantity = 0F;
	private Float tolerance = 0F;
	@Column(name="container_qty")
	private Integer containerQty = 0;
	private String unit;
	private Integer status = 0;
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
	public Float getQuantity() {
		return quantity;
	}
	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}
	public Integer getContainerQty() {
		return containerQty;
	}
	public void setContainerQty(Integer containerQty) {
		this.containerQty = containerQty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Float getActTotal() {
		return actTotal;
	}
	public void setActTotal(Float actTotal) {
		this.actTotal = actTotal;
	}
	public Float getActQuantity() {
		return actQuantity;
	}
	public void setActQuantity(Float actQuantity) {
		this.actQuantity = actQuantity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getTolerance() {
		return tolerance;
	}
	public void setTolerance(Float tolerance) {
		this.tolerance = tolerance;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
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
	public Integer getReplenish() {
		return replenish;
	}
	public void setReplenish(Integer replenish) {
		this.replenish = replenish;
	}

}
