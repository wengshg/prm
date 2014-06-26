package com.prm.resources.system;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.system.Line;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel="line", path = "line")
public interface LineRepository extends PrmRepository<Line, Long> {
	List<Line> findByEnable(@Param("enable") Integer enable);
	
	List<Line> findByCode(@Param("code") String code);

	List<Line> findByName(@Param("name") String name);

	List<Line> findByCodeLike(@Param("code") String code);

	List<Line> findByNameLike(@Param("name") String name);
}
