package com.prm.resources.work;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workordermaterial", path = "workordermaterial")
public interface WorkOrderMaterialRepository extends
		PrmRepository<WorkOrderMaterial, Long> {
	/**
	 * Search by workorder ID.
	 * @param wid
	 * @return
	 */
	List<WorkOrderMaterial> findByWid(@Param("wid") Long wid);
	
	/**
	 * 
	 * @param wid
	 * @param mid
	 * @return
	 */
	List<WorkOrderMaterial> findByWidAndMid(@Param("wid") Long wid, @Param("mid") Long mid);
}
