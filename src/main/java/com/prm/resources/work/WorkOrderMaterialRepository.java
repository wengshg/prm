package com.prm.resources.work;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "workordermaterial", path = "workordermaterial")
public interface WorkOrderMaterialRepository extends
		PrmRepository<WorkOrderMaterial, Long> {
	/**
	 * Search by workorder ID.
	 * @param wid
	 * @return
	 */
	List<WorkOrderMaterial> findByWid(@Param("wid") Long wid);
	
	/**
	 * 
	 * @param wid
	 * @param mid
	 * @return
	 */
	List<WorkOrderMaterial> findByWidAndMid(@Param("wid") Long wid, @Param("mid") Long mid);
	
	
	/**
	 * Add a new material container.
	 * @param total
	 * @param quantity
	 */
	@Modifying
	@Query(value="update workorder_material set actl_total = actl_total + :total, " +
			" actl_quantity = actl_quantity + :quantity, container_qty = container_qty + 1 " +
			" where id = :id", nativeQuery=true)
	void addMaterialContainer(@Param("id") Long id, @Param("total") Float total, @Param("quantity") Float quantity);
}
