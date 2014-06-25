package com.prm.resources.work;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.prm.models.work.WorkOrderLog;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workorderlog", path = "workorderlog")
public interface WorkOrderLogRepository extends
		PrmRepository<WorkOrderLog, Long> {
	
	@RestResource(exported = false)
	WorkOrderLog save(WorkOrderLog wol);

	@RestResource(exported = false)
	void delete(Long id);

	/**
	 * Search the log for containers.
	 * 
	 * @param wid
	 * @param mid
	 * @param sequence
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMidAndSequence(@Param("wid") Long wid,
			@Param("mid") Long mid, @Param("sequence") Integer sequence);

	/**
	 * Search the log for the container with specific status.
	 * 
	 * @param wid
	 * @param mid
	 * @param sequence
	 * @param status
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMidAndSequenceAndStatus(@Param("wid") Long wid,
			@Param("mid") Long mid, @Param("sequence") Integer sequence, @Param("status") Integer status);

	/**
	 * Search all the logs for workorder materials.
	 * 
	 * @param wid
	 * @param mid
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMid(@Param("wid") Long wid,
			@Param("mid") Long mid);

	/**
	 * Search only the logs for workorder material.
	 * 
	 * @param wid
	 * @param mid
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMidAndSequenceIsNull(@Param("wid") Long wid,
			@Param("mid") Long mid);

	/**
	 * Search only the logs for workorder material with specific status.
	 * 
	 * @param wid
	 * @param mid
	 * @param status
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMidAndStatusAndSequenceIsNull(@Param("wid") Long wid,
			@Param("mid") Long mid, @Param("status") Integer status);

	/**
	 * Search all the logs for workorders.
	 * 
	 * @param wid
	 * @return
	 */
	List<WorkOrderLog> findByWid(@Param("wid") Long wid);
	
	/**
	 * Search only the logs for workorders.
	 * 
	 * @param wid
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMidIsNullAndSequenceIsNull(@Param("wid") Long wid);
	
	/**
	 * Search only the logs for workorder with the specific status.
	 * 
	 * @param wid
	 * @param status
	 * @return
	 */
	List<WorkOrderLog> findByWidAndStatusAndMidIsNullAndSequenceIsNull(@Param("wid") Long wid, @Param("status") Integer status);

}
