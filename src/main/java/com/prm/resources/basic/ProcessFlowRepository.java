package com.prm.resources.basic;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.ProcessFlow;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "processflow", path = "processflow")
public interface ProcessFlowRepository extends PrmRepository<ProcessFlow, Long> {

}
