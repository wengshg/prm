package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.Bom;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "bom", path = "bom")
public interface BomRepository extends PrmRepository<Bom, Long> {
	/**
	 * search all the boms for specific pid.
	 * @param pid
	 * @return
	 */
	List<Bom> findByPid(@Param("pid") Long pid);
	
	
	List<Bom> findByCode(@Param("code") String code);
	
	List<Bom> findByCodeLike(@Param("code") String code);
	
	List<Bom> findByName(@Param("name") String name);

	List<Bom> findByNameLike(@Param("name") String name);
}
