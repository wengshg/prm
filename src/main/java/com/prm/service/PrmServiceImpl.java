package com.prm.service;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prm.Exception.PrmRuntimeException;
import com.prm.models.basic.Bom;
import com.prm.models.basic.BomItem;
import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderContainer;
import com.prm.models.work.WorkOrderLog;
import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.basic.BomItemRepository;
import com.prm.resources.basic.BomRepository;
import com.prm.resources.work.WorkOrderContainerRepository;
import com.prm.resources.work.WorkOrderLogRepository;
import com.prm.resources.work.WorkOrderMaterialRepository;
import com.prm.resources.work.WorkOrderRepository;

@Service
public class PrmServiceImpl implements PrmService {
	private static Logger logger = LoggerFactory.getLogger(PrmServiceImpl.class);
	
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
	public WorkOrder create(Long uid, WorkOrder workOrder) {
		WorkOrder swo = workOrderRepository.save(workOrder);
		if (swo != null) {
			//start with workorder_material
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
			
			//Add WorkOrderLog
			generateLog(uid, swo);
		}
		return swo;
	}
	
	private void generateLog(Long uid, WorkOrder workOrder) {
		WorkOrderLog log = new WorkOrderLog();
		log.setStatus(workOrder.getStatus());
		log.setCreatedUid(uid);
		log.setWid(workOrder.getId());
		log.setCreatedTime(Calendar.getInstance().getTimeInMillis());
		workOrderLogRepository.save(log);
	}
	
	private void generateLog(Long uid, WorkOrderMaterial workOrderMaterial) {
		WorkOrderLog log = new WorkOrderLog();
		log.setStatus(workOrderMaterial.getStatus());
		log.setCreatedUid(uid);
		log.setWid(workOrderMaterial.getWid());
		log.setMid(workOrderMaterial.getWid());
		log.setCreatedTime(Calendar.getInstance().getTimeInMillis());
		workOrderLogRepository.save(log);
	}
	
	/**
	 * 1. Create a WorkOrderContainer
	 * 2. Update WorkOrderMaterial 3 fields (actQantity, actTotal, containerQty)
	 * 3. Create a WorkOrderLog.
	 */
	@Transactional
	@Override
	public WorkOrderContainer create(Long uid, WorkOrderContainer workOrderContainer) {
		Float ctnTotal = workOrderContainer.getTotal();
		Float cntQuty = workOrderContainer.getQuantity();
		WorkOrderContainer woc = workOrderContainerRepository.save(workOrderContainer);
		if (woc != null) {
			//update workordermaterial
			Long wid = woc.getWid();
			Long mid = woc.getMid();
			List<WorkOrderMaterial> woms = workOrderMaterialRepository.findByWidAndMid(wid, mid);
			if (woms == null || woms.size() != 1) {
				throw new PrmRuntimeException("WorkOrderMaterial find issue, key of wid: " + wid + ", mid: " + mid);
			}
			
			WorkOrderMaterial wom = woms.get(0);
			Long womId = wom.getId();
			workOrderMaterialRepository.addMaterialContainer(womId, ctnTotal, cntQuty);
			
			//Add workorderlog.
			generateLog(uid, woc);
		}
		return woc;
	}
	
	private void generateLog(Long uid, WorkOrderContainer workOrderContainer) {
		WorkOrderLog log = new WorkOrderLog();
		log.setMid(workOrderContainer.getMid());
		log.setSequence(workOrderContainer.getSequence());
		log.setStatus(workOrderContainer.getStatus());
		log.setCreatedUid(uid);
		log.setWid(workOrderContainer.getWid());
		log.setCreatedTime(Calendar.getInstance().getTimeInMillis());
		workOrderLogRepository.save(log);
	}

	@Override
	public void update(Long uid, WorkOrderContainer workOrderContainer) {
		if (workOrderContainer.getStatus() == null) {
			throw new PrmRuntimeException("WorkOrderContainer.status is null.");
		}
		Long id = workOrderContainer.getId();
		WorkOrderContainer wocDB = workOrderContainerRepository.findOne(id);
		if (wocDB != null) {
			wocDB.setStatus(workOrderContainer.getStatus());
			workOrderContainerRepository.save(wocDB);
			
			//Add workorderlog
			generateLog(uid, wocDB);
		}
	}

	@Override
	public void update(Long uid, WorkOrderMaterial workOrderMaterial) {
		if (workOrderMaterial.getStatus() == null) {
			throw new PrmRuntimeException("WorkOrderMaterial.status is null.");
		}
		Long id = workOrderMaterial.getId();
		WorkOrderMaterial womDB = workOrderMaterialRepository.findOne(id);
		if (womDB != null) {
			womDB.setStatus(workOrderMaterial.getStatus());
			workOrderMaterialRepository.save(womDB);
			
			//Add workorderlog
			generateLog(uid, womDB);
		}
	}

	@Override
	public void update(Long uid, WorkOrder workOrder) {
		if (workOrder.getStatus() == null) {
			throw new PrmRuntimeException("WorkOrder.status is null.");
		}
		Long id = workOrder.getId();
		WorkOrder woDB = workOrderRepository.findOne(id);
		if (woDB != null) {
			woDB.setStatus(workOrder.getStatus());
			workOrderRepository.save(woDB);
			
			//Add workorderlog
			generateLog(uid, woDB);
		}
	}

}
