package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.BomItem;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "bomitem", path = "bomitem")
public interface BomItemRepository extends PrmRepository<BomItem, Long> {
	
	List<BomItem> findByBid(@Param("bid") Long bid);
	
	List<BomItem> findByBidOrderByQuantity(@Param("bid") Long bid);
}
