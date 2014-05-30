package com.prm.resources.work;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.WorkOrderContainer;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workordercontainer", path = "workordercontainer")
public interface WorkOrderContainerRepository extends
		PrmRepository<WorkOrderContainer, Long> {

}
