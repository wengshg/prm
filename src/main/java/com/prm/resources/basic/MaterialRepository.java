package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.Material;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "material", path = "material")
public interface MaterialRepository extends PrmRepository<Material, Long> {
	
	/**
	 * find materials by code
	 * @param code
	 * @return
	 */
	List<Material> findByCode(@Param("code") String code);

	List<Material> findByCodeLike(@Param("code") String code);

	List<Material> findByName(@Param("name") String name);

	List<Material> findByNameLike(@Param("name") String name);
}
