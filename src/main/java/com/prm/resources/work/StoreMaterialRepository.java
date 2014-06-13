package com.prm.resources.work;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.StoreMaterial;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "storematerial", path = "storematerial")
public interface StoreMaterialRepository extends
		PrmRepository<StoreMaterial, Long> {
	
	/**
	 * Search by roomid and order by signed date in descend.
	 * @param rid
	 * @return
	 */
	List<StoreMaterial> findByRidOrderBySignedDateDesc(@Param("rid") Long rid);
}
