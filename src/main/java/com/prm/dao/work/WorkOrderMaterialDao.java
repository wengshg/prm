package com.prm.dao.work;

public interface WorkOrderMaterialDao {
	/**
	 * 
	 * @param wid
	 * @param status
	 * @return
	 */
	boolean isAllMaterialsStatusAS(Long wid, Integer status);

	/**
	 * Remove all the records related to the workorder id.
	 * @param wid
	 */
	void delete(Long wid);
}
