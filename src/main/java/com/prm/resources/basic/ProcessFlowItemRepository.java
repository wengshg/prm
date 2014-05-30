package com.prm.resources.basic;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.ProcessFlowItem;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "processflowitem", path = "processflowitem")
public interface ProcessFlowItemRepository extends
		PrmRepository<ProcessFlowItem, Long> {

}
