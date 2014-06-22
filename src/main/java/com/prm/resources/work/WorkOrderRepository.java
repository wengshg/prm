package com.prm.resources.work;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.prm.models.work.WorkOrder;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workorder", path = "workorder")
public interface WorkOrderRepository extends PrmRepository<WorkOrder, Long> {
	
	@RestResource(exported = false)
	WorkOrder save(WorkOrder w);
	
	@RestResource(exported = false)
	void delete(Long id);
		
	List<WorkOrder> findByCode(@Param("code") String code);
	
	@Query("select o from workorder o where o.workStartDate <= :date and o.workEndDate >= :date")
	List<WorkOrder> findByDate(@Param("date") Long date);
	

}
