package com.prm.resources.work;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.WorkOrder;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workorder", path = "workorder")
public interface WorkOrderRepository extends PrmRepository<WorkOrder, Long> {

}
