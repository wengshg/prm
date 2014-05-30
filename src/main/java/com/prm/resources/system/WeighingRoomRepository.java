package com.prm.resources.system;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.system.WeighingRoom;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "weighingroom", path = "weighingroom")
public interface WeighingRoomRepository extends
		PrmRepository<WeighingRoom, Long> {

}
