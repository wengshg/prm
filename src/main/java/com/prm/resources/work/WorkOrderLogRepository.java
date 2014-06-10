package com.prm.resources.work;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.prm.models.work.WorkOrderLog;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workorderlog", path = "workorderlog")
public interface WorkOrderLogRepository extends PrmRepository<WorkOrderLog, Long> {

}
