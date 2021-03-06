package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.ProcessFlow;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "processflow", path = "processflow")
public interface ProcessFlowRepository extends PrmRepository<ProcessFlow, Long> {
	/**
	 * search ProcessFlow with the bid.
	 * @param bid
	 * @return
	 */
	List<ProcessFlow> findByBid(@Param("bid") Long bid);
	
	List<ProcessFlow> findByCode(@Param("code") String code);
	
	List<ProcessFlow> findByCodeLike(@Param("code") String code);
	
	List<ProcessFlow> findByName(@Param("name") String name);

	List<ProcessFlow> findByNameLike(@Param("name") String name);
}
