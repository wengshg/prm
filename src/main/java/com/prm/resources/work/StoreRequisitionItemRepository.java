package com.prm.resources.work;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.StoreRequisitionItem;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "storerequisitionitem", path = "storerequisitionitem")
public interface StoreRequisitionItemRepository extends PrmRepository<StoreRequisitionItem, Long> {
	
	List<StoreRequisitionItem> findByQid(@Param("qid") Long qid);
}
