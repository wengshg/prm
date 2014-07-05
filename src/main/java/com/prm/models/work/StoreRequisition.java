package com.prm.models.work;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="store_requisition")
public class StoreRequisition {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long sid;
	private Long wid;
	private String code;
	@Column(name="created_date")
	private Long createdDate;
	@Column(name="created_uid")
	private Long creator;
	@Column(name="signed_date")
	private Long signedDate;
	@Column(name="signed_uid")
	private Long signer;
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
	public Long getWid() {
		return wid;
	}
	public void setWid(Long wid) {
		this.wid = wid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Long getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(Long signedDate) {
		this.signedDate = signedDate;
	}
	public Long getSigner() {
		return signer;
	}
	public void setSigner(Long signer) {
		this.signer = signer;
	}
	
}
