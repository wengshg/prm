package com.prm.resources.system;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.system.WeighingRoom;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "weighingroom", path = "weighingroom")
public interface WeighingRoomRepository extends
		PrmRepository<WeighingRoom, Long> {
	
	List<WeighingRoom> findByCode(@Param("code") String code);
	
	List<WeighingRoom> findByCodeLike(@Param("code") String code);
	
	List<WeighingRoom> findByName(@Param("name") String name);

	List<WeighingRoom> findByNameLike(@Param("name") String name);
}
