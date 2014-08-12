package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.ProcessFlowItem;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "processflowitem", path = "processflowitem")
public interface ProcessFlowItemRepository extends
		PrmRepository<ProcessFlowItem, Long> {
	
	List<ProcessFlowItem> findByFid(@Param("fid") Long fid);
	
	List<ProcessFlowItem> findByFidOrderBySequenceAsc(@Param("fid") Long fid);
	
	List<ProcessFlowItem> findByFidAndMid(@Param("fid") Long fid, @Param("mid") Long mid);
}
