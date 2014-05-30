package com.prm.resources.work;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workordermaterial", path = "workordermaterial")
public interface WorkOrderMaterialRepository extends
		PrmRepository<WorkOrderMaterial, Long> {

}
