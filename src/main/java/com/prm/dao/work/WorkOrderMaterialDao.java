package com.prm.dao.work;

public interface WorkOrderMaterialDao {
	/**
	 * 
	 * @param wid
	 * @param status
	 * @return
	 */
	boolean isAllMaterialsStatusAS(Long wid, int status);

}
