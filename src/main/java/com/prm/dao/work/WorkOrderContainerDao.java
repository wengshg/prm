package com.prm.dao.work;

public interface WorkOrderContainerDao {
	/**
	 * This is to check if all the containers status are the same as the
	 * expected status.
	 * 
	 * @param wid
	 * @param mid
	 * @param status
	 * @return
	 */
	boolean isAllContainersStatusAs(Long wid, Long mid, Integer status);
	
	/**
	 * Remove all the records related to the workorder id.
	 * @param wid
	 */
	void delete(Long wid);
}