package com.prm.resources.work;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.WorkOrderContainer;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workordercontainer", path = "workordercontainer")
public interface WorkOrderContainerRepository extends
		PrmRepository<WorkOrderContainer, Long> {
	
	/**
	 * 
	 * @param wid
	 * @param mid
	 * @return
	 */
	List<WorkOrderContainer> findByWidAndMid(@Param("wid") Long wid, @Param("mid") Long mid);
}
