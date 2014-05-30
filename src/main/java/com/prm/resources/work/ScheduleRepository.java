package com.prm.resources.work;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.Schedule;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "schedule", path = "schedule")
public interface ScheduleRepository extends PrmRepository<Schedule, Long> {

}
