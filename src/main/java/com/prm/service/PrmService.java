package com.prm.service;

import java.util.List;

import com.prm.models.basic.BomItem;
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
	 * 	 * Only just created workorder can be deleted.
	 * delete the workorder and meanwhile delete the cascade talbes 
	 * includes workordercontainer, workordermaterial and workorderlog.
	 * @param wid
	 * @return
	 */
	void delete(Long wid);

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
	 * @param workOrderMaterial
	 * @param wid
	 * @param mid
	 */
	Long update(Long uid, WorkOrderMaterial workOrderMaterial, Long wid, Long mid);

	/**
	 * Currently only support status updating.
	 * @param uid
	 * @param workOrder
	 */
	void update(Long uid, WorkOrder workOrder, boolean cascade);
	
	/**
	 * return the estimated materials base on the bom id and quantity.
	 * @param bid
	 * @param quantity
	 * @return
	 */
	List<BomItem> getEstimatedMaterials(Long bid, Float quantity);

}
