package com.prm.service;

import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderContainer;
import com.prm.models.work.WorkOrderMaterial;

public interface PrmService {
	/**
	 * 
	 * @param workOrder
	 * @return
	 */
	WorkOrder create(Long uid, WorkOrder workOrder);

	/**
	 * 
	 * @param workOrderContainer
	 * @return
	 */
	WorkOrderContainer create(Long uid, WorkOrderContainer workOrderContainer);
		
	/**
	 * Currently only support status updating.
	 * @param workOrderContainer
	 * @return
	 */
	void update(Long uid, WorkOrderContainer workOrderContainer);
	
	/**
	 * Currently only support status updating.
	 * @param uid
	 * @param workOrderMaterial
	 */
	void update(Long uid, WorkOrderMaterial workOrderMaterial);
	
	/**
	 * Currently only support status updating.
	 * @param uid
	 * @param workOrder
	 */
	void update(Long uid, WorkOrder workOrder, boolean cascade);

}
