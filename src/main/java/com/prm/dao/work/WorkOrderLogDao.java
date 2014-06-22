package com.prm.dao.work;

public interface WorkOrderLogDao {
	/**
	 * Remove all the records related to the workorder id.
	 * @param wid
	 */
	void delete(Long wid);
}
