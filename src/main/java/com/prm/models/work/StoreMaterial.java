package com.prm.models.work;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="store_material")
public class StoreMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long mid;
	private long rid;
	@Column(name="original_code")
	private String originalCode;
	private float quantity;
	private String unit;
	@Column(name="signed_date")
	@Temporal(TemporalType.DATE)
	private Date signedDate;
	@Column(name="signed_uid")
	private Long signedUid;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public long getRid() {
		return rid;
	}
	public void setRid(long rid) {
		this.rid = rid;
	}
	public String getOriginalCode() {
		return originalCode;
	}
	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
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
	public Date getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}
	public Long getSignedUid() {
		return signedUid;
	}
	public void setSignedUid(Long signedUid) {
		this.signedUid = signedUid;
	}
	
}
