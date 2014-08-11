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
	
	List<WorkOrder> findByCodeLike(@Param("code") String code);

	@Query("select o from workorder o where o.workStartDate <= :date and o.workEndDate >= :date")
	List<WorkOrder> findByDate(@Param("date") Long date);
	
	@Query("select o from workorder o where ((:startdate < :enddate) and NOT ((o.workEndDate < :startdate) or (o.workStartDate > :enddate)))")
	List<WorkOrder> findByDateRange(@Param("startdate") Long startdate, @Param("enddate") Long enddate);
	
	List<WorkOrder> findBySid(@Param("sid") Long sid);
	
	List<WorkOrder> findByStatus(@Param("status") Integer status);
	
	List<WorkOrder> findByLidAndStatus(@Param("lid") Long lid, @Param("status") Integer status);
	
	@Query(value = "select distinct wo.* from workorder wo, workorder_container wc where wo.id = wc.wid and wc.eid=:eid and wc.status=:status order by wo.code", nativeQuery = true)
	List<WorkOrder> findByWorderContainerEidAndStatus(@Param("eid") Long eid,
			@Param("status") Integer status);
}
