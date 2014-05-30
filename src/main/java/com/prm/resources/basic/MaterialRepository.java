package com.prm.resources.basic;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.Material;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "material", path = "material")
public interface MaterialRepository extends PrmRepository<Material, Long> {

}
