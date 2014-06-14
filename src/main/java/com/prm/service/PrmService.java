package com.prm.service;

import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderContainer;

public interface PrmService {
	/**
	 * 
	 * @param workOrder
	 * @return
	 */
	WorkOrder create(WorkOrder workOrder);

	/**
	 * 
	 * @param workOrderContainer
	 * @return
	 */
	WorkOrderContainer create(WorkOrderContainer workOrderContainer);
		
	/**
	 * 
	 * @param workOrderContainer
	 * @return
	 */
	void update(WorkOrderContainer workOrderContainer);		

}
