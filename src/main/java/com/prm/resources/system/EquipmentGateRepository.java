package com.prm.resources.system;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.system.EquipmentGate;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "equipmentgate", path = "equipmentgate")
public interface EquipmentGateRepository extends
		PrmRepository<EquipmentGate, Long> {

}
