package com.prm.resources.system;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.system.Equipment;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "equipment", path = "equipment")
public interface EquipmentRepository extends PrmRepository<Equipment, Long> {
	List<Equipment> findByEnable(@Param("enable") int enable);
	
	Equipment findByCode(@Param("code") String code);

	Equipment findByName(@Param("name") String name);
	
	/**
	 * Search certain type equipments in the specified line.
	 * @param lid
	 * @param type
	 * @return
	 */
	List<Equipment> findByLidAndType(@Param("lid")long lid, @Param("type")String type);
	
	/**
	 * 
	 * @return
	 */
	@Query("select distinct o.type from Equipment o ")
	List<String> findTypeDistinctOrderByTypeAsc();
	
}
