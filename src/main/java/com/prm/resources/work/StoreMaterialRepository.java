package com.prm.resources.work;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.StoreMaterial;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "storematerial", path = "storematerial")
public interface StoreMaterialRepository extends
		PrmRepository<StoreMaterial, Long> {

}
