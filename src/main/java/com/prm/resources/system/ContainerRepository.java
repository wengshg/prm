package com.prm.resources.system;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.prm.models.system.Container;
import com.prm.models.system.Equipment;
import com.prm.resources.PrmRepository;

public interface ContainerRepository extends PrmRepository<Container, Long> {
	List<Equipment> findByCode(@Param("code") String code);

	List<Equipment> findByName(@Param("name") String name);
	
	List<Equipment> findByCodeLike(@Param("code") String code);

	List<Equipment> findByNameLike(@Param("name") String name);

}
