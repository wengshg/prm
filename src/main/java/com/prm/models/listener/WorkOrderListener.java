package com.prm.models.listener;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;

import com.prm.models.work.WorkOrder;

public class WorkOrderListener {	
	@PrePersist
	public void prePersist(WorkOrder workOrder) {
		
	}
	
	@PostPersist
	public void postPersist(WorkOrder workOrder) {
	}

	@PostUpdate
	public void postUpdate(WorkOrder workOrder) {
//		System.out.println("after update" + workOrder.getId());
	}

}
