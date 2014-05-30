package com.prm.resources.basic;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.BomItem;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "bomitem", path = "bomitem")
public interface BomItemRepository extends PrmRepository<BomItem, Long> {

}
