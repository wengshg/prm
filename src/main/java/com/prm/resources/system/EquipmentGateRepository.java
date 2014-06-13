package com.prm.resources.system;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.system.EquipmentGate;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "equipmentgate", path = "equipmentgate")
public interface EquipmentGateRepository extends
		PrmRepository<EquipmentGate, Long> {
	List<EquipmentGate> findByLidAndType(@Param("lid")long lid, @Param("type")String type);
	
	List<EquipmentGate> findByEidAndType(@Param("eid")long eid, @Param("type")String type);
}