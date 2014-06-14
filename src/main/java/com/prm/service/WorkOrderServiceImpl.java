package com.prm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prm.models.basic.Bom;
import com.prm.models.basic.BomItem;
import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderContainer;
import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.basic.BomItemRepository;
import com.prm.resources.basic.BomRepository;
import com.prm.resources.work.WorkOrderContainerRepository;
import com.prm.resources.work.WorkOrderLogRepository;
import com.prm.resources.work.WorkOrderMaterialRepository;
import com.prm.resources.work.WorkOrderRepository;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {
	private static Logger logger = LoggerFactory.getLogger(WorkOrderServiceImpl.class);
	
	@Autowired
	WorkOrderRepository workOrderRepository;
	
	@Autowired
	BomRepository bomRepository;

	@Autowired
	BomItemRepository bomItemRepository;
	
	@Autowired
	WorkOrderMaterialRepository workOrderMaterialRepository;
	
	@Autowired
	WorkOrderContainerRepository workOrderContainerRepository;
	
	@Autowired
	WorkOrderLogRepository workOrderLogRepository;
	
	@Transactional
	@Override
	public WorkOrder create(WorkOrder workOrder) {
		WorkOrder swo = workOrderRepository.save(workOrder);
		if (swo != null) {
			Bom bom = bomRepository.findOne(swo.getBid());
			Float bomQty = bom.getQuantity();
			List<BomItem> bomItems = bomItemRepository.findByBid(swo.getBid());
			Float woQty = workOrder.getQuantity();
			for (BomItem bomItem : bomItems) {
				WorkOrderMaterial wom = new WorkOrderMaterial();
				Float biQty = bomItem.getQuantity();
				Float qty = woQty * (biQty / bomQty);
				if (logger.isTraceEnabled()) {
					logger.trace("Qty of material: " + bomItem.getMid() + ", for WorkOrder: " + swo.getId() + "is : " + qty);
				}
				wom.setQuantity(qty);
				wom.setMid(bomItem.getMid());
				wom.setUnit(bomItem.getUnit());
				wom.setWid(swo.getId());
				WorkOrderMaterial savedWOM = workOrderMaterialRepository.save(wom);
				if (logger.isInfoEnabled()) {
					logger.info("Saved work order material: " + savedWOM.getId());
				}
			}
		}
		return swo;
	}

	@Transactional
	@Override
	public WorkOrderContainer create(WorkOrderContainer workOrderContainer) {
		WorkOrderContainer woc = workOrderContainerRepository.save(workOrderContainer);
		if (woc != null) {
			//TODO
			
			
		}
		return woc;
	}

	@Override
	public void update(WorkOrderContainer workOrderContainer) {
		WorkOrderContainer woc = workOrderContainerRepository.save(workOrderContainer);
		if (woc != null) {
			//TODO
			
			
		}
	}

}
