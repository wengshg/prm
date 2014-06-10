package com.prm.models.listener;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import com.prm.models.work.WorkOrder;

public class WorkOrderListener {	
	@PostPersist
	public void postPersist(WorkOrder workOrder) {
	}

	@PostUpdate
	public void postUpdate(WorkOrder workOrder) {
		System.out.println("after update" + workOrder.getId());
	}

}
