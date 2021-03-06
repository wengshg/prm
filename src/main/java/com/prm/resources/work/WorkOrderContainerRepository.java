package com.prm.resources.work;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.prm.models.work.WorkOrderContainer;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workordercontainer", path = "workordercontainer")
public interface WorkOrderContainerRepository extends
		PrmRepository<WorkOrderContainer, Long> {
	
	@RestResource(exported = false)
	WorkOrderContainer save(WorkOrderContainer woc);
	
	@RestResource(exported = false)
	void delete(Long id);
	
	/**
	 * find by wid
	 * 
	 * @param wid
	 * @return
	 */
	List<WorkOrderContainer> findByWid(@Param("wid") Long wid);
	
	/**
	 * find by wid and mid
	 * 
	 * @param wid
	 * @param mid
	 * @return
	 */
	List<WorkOrderContainer> findByWidAndMid(@Param("wid") Long wid,
			@Param("mid") Long mid);

	/**
	 * find by wid, mid and sequence
	 * 
	 * @param wid
	 * @return
	 */
	List<WorkOrderContainer> findByWidAndMidAndSequence(@Param("wid") Long wid,
			@Param("mid") Long mid, @Param("sequence") Integer sequence);
	
	
	List<WorkOrderContainer> findByWidAndStatus(@Param("wid") Long wid, @Param("status") Integer status);

	@Query(value = "select wc.* from workorder_container wc, process_flow_item pfi where wc.mid=pfi.mid and wc.fid=pfi.fid  and wid=:wid and eid=:eid order by pfi.sequence asc, wc.sequence asc", nativeQuery = true)
	List<WorkOrderContainer> findByWidAndEidOrderBySeqAndInPFI(@Param("wid") Long wid, @Param("eid") Long eid);
}
