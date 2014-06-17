package com.prm.models.report;

public class WorkOrderContainerRpt {
	private Long mid;
	
	private Float sumTotal;
	
	private Float sumQuantity;
	
	private Integer count;

	public Float getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(Float sumTotal) {
		this.sumTotal = sumTotal;
	}

	public Float getSumQuantity() {
		return sumQuantity;
	}

	public void setSumQuantity(Float sumQuantity) {
		this.sumQuantity = sumQuantity;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}
	
	
}
