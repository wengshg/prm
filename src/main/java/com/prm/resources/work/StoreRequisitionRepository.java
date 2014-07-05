package com.prm.resources.work;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.StoreRequisition;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "storerequisition", path = "storerequisition")
public interface StoreRequisitionRepository extends PrmRepository<StoreRequisition, Long> {

	List<StoreRequisition> findByCode(@Param("code") String code);
	
	List<StoreRequisition> findByCodeLike(@Param("code") String code);
}
