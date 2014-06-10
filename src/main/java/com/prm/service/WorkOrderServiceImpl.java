package com.prm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prm.models.basic.BomItem;
import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.basic.BomItemRepository;
import com.prm.resources.work.WorkOrderMaterialRepository;
import com.prm.resources.work.WorkOrderRepository;

@Component
public class WorkOrderServiceImpl implements WorkOrderService {
	@Autowired
	WorkOrderRepository workOrderRepository;
	
	@Autowired
	BomItemRepository bomItemRepository;
	
	@Autowired
	WorkOrderMaterialRepository workOrderMaterialRepository;

	@Override
	public WorkOrder create(WorkOrder workOrder) {
		WorkOrder swo = workOrderRepository.save(workOrder);
		List<BomItem> bomItems = bomItemRepository.findByBid(swo.getBid());
		long woQty = workOrder.getQuantity();
		for (BomItem bomItem : bomItems) {
			WorkOrderMaterial wom = new WorkOrderMaterial();
			wom.setQuantity(bomItem.getQuantity());
		}
		System.out.println("saved id: " + swo.getId());
		return swo;
	}

}
