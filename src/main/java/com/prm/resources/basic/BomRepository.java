package com.prm.resources.basic;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.Bom;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "bom", path = "bom")
public interface BomRepository extends PrmRepository<Bom, Long> {

}
