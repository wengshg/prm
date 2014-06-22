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
	 * Search the log for workorder materials.
	 * 
	 * @param wid
	 * @param mid
	 * @param sequence
	 * @return
	 */
	List<WorkOrderLog> findByWidAndMid(@Param("wid") Long wid,
			@Param("mid") Long mid);

	/**
	 * Search the log for workorders.
	 * 
	 * @param wid
	 * @param mid
	 * @param sequence
	 * @return
	 */
	List<WorkOrderLog> findByWid(@Param("wid") Long wid);

}
