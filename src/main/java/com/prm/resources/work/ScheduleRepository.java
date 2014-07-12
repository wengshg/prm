package com.prm.resources.work;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.Schedule;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "schedule", path = "schedule")
public interface ScheduleRepository extends PrmRepository<Schedule, Long> {
	
	@Query("select o from Schedule o where o.schdStartDate <= :date and o.schdEndDate >= :date")
	List<Schedule> findByDT(@Param("date") Long date);

	
	List<Schedule> findByCode(@Param("code") String code);
	
	List<Schedule> findByCodeLike(@Param("code") String code);
}
