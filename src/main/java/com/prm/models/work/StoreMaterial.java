package com.prm.models.work;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="store_material")
public class StoreMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long mid;
	private Long rid;
	private Long sid;
	private Long wid;
	@Column(name="original_code")
	private String originalCode;
	private Float quantity = 0F;
	private String unit;
	@Column(name="signed_date")
	private Long signedDate = Calendar.getInstance().getTimeInMillis();
	@Column(name="signed_uid")
	private Long signedUid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public Long getRid() {
		return rid;
	}
	public void setRid(Long rid) {
		this.rid = rid;
	}
	public String getOriginalCode() {
		return originalCode;
	}
	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
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
	public Long getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(Long signedDate) {
		this.signedDate = signedDate;
	}
	public Long getSignedUid() {
		return signedUid;
	}
	public void setSignedUid(Long signedUid) {
		this.signedUid = signedUid;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public Long getWid() {
		return wid;
	}
	public void setWid(Long wid) {
		this.wid = wid;
	}
	
}
