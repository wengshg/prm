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
	private static Logger logger = LoggerFactory
			.getLogger(PrmServiceImpl.class);

	//workorder -> workordermaterial
	private static final int STATUS_APPROVED = 1;
	//workorder <- workordermaterial 
	private static final int STATUS_ALLOCATED = 2;
	//workorder -> workordermaterial -> workordercontainer
	private static final int STATUS_DOUBLE_CHECKED = 3;
	//workorder <- workordermaterial <- workordercontainer
	private static final int STATUS_DREW = 4;
	//workorder <- workordermaterial <- workordercontainer
	private static final int STATUS_FEED = 5;
	//workorder -> workordermaterial -> workordercontainer
	private static final int STATUS_COMPLETED = 9;
		
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
			// start with workorder_material
			Bom bom = bomRepository.findOne(swo.getBid());
			Float bomQty = bom.getQuantity();
			List<BomItem> bomItems = bomItemRepository.findByBid(swo.getBid());
			Float woQty = workOrder.getQuantity();
			for (BomItem bomItem : bomItems) {
				WorkOrderMaterial wom = new WorkOrderMaterial();
				Float biQty = bomItem.getQuantity();
				Float qty = woQty * (biQty / bomQty);
				if (logger.isTraceEnabled()) {
					logger.trace("Qty of material: " + bomItem.getMid()
							+ ", for WorkOrder: " + swo.getId() + "is : " + qty);
				}
				wom.setQuantity(qty);
				wom.setMid(bomItem.getMid());
				wom.setUnit(bomItem.getUnit());
				wom.setWid(swo.getId());
				WorkOrderMaterial savedWOM = workOrderMaterialRepository
						.save(wom);
				if (logger.isInfoEnabled()) {
					logger.info("Saved work order material: "
							+ savedWOM.getId());
				}
			}

			// Add WorkOrderLog
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
	 * 1. Create a WorkOrderContainer 2. Update WorkOrderMaterial 3 fields
	 * (actQantity, actTotal, containerQty) 3. Create a WorkOrderLog.
	 */
	@Transactional
	@Override
	public WorkOrderContainer create(Long uid,
			WorkOrderContainer workOrderContainer) {
		Float ctnTotal = workOrderContainer.getTotal();
		Float cntQuty = workOrderContainer.getQuantity();
		WorkOrderContainer woc = workOrderContainerRepository
				.save(workOrderContainer);
		if (woc != null) {
			// update workordermaterial
			Long wid = woc.getWid();
			Long mid = woc.getMid();
			List<WorkOrderMaterial> woms = workOrderMaterialRepository
					.findByWidAndMid(wid, mid);
			if (woms == null || woms.size() != 1) {
				throw new PrmRuntimeException(
						"WorkOrderMaterial find issue, key of wid: " + wid
								+ ", mid: " + mid);
			}

			WorkOrderMaterial wom = woms.get(0);
			Long womId = wom.getId();
			workOrderMaterialRepository.addMaterialContainer(womId, ctnTotal,
					cntQuty);

			// Add workorderlog.
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

	@Transactional
	@Override
	public void update(Long uid, WorkOrderContainer workOrderContainer) {
		Integer status = workOrderContainer.getStatus();
		if (status == null) {
			throw new PrmRuntimeException("WorkOrderContainer.status is null.");
		}
		Long id = workOrderContainer.getId();
		WorkOrderContainer wocDB = workOrderContainerRepository.findOne(id);
		if (wocDB != null) {
			Integer originalStatus = wocDB.getStatus();
			wocDB.setStatus(status);

			saveWithLog(uid, wocDB);

			//check if this is to update the final record.
			//TODO
			if (chkIfUpdateUpChain(status.intValue(), wocDB)) {
				//save workordermaterial
				Long wid = wocDB.getWid();
				Long mid = wocDB.getMid();
				List<WorkOrderMaterial> woms = workOrderMaterialRepository
						.findByWidAndMid(wid, mid);
				if (woms != null && woms.size() == 1) {
					WorkOrderMaterial wom = woms.get(0);
					if (status.intValue() != wom.getStatus().intValue() ) {
						wom.setStatus(status);
						
						saveWithLog(uid, wom);
					}
					//save workorder
					WorkOrder workOrder = workOrderRepository.findOne(wid);
					if (workOrder != null) {
						if (status.intValue() != workOrder.getStatus().intValue()) {
							workOrder.setStatus(status);
							
							saveWithLog(uid, workOrder);
						}
					} else {
						throw new PrmRuntimeException("Invalid workorder ID: " + wid);
					}
				} else {
					throw new PrmRuntimeException(
							"WorkOrderMaterial data is not unique or null, wid: " + wid
									+ ", mid: " + mid);
				}
			}
		}
	}

	private void saveWithLog(Long uid, WorkOrderContainer workOrderContainer) {
		workOrderContainerRepository.save(workOrderContainer);

		// Add workorderlog
		generateLog(uid, workOrderContainer);
	}

	@Transactional
	@Override
	public void update(Long uid, WorkOrderMaterial workOrderMaterial) {
		Integer status = workOrderMaterial.getStatus();
		if (status == null) {
			throw new PrmRuntimeException("WorkOrderMaterial.status is null.");
		}
		Long id = workOrderMaterial.getId();
		WorkOrderMaterial womDB = workOrderMaterialRepository.findOne(id);
		if (womDB != null) {
			womDB.setStatus(status);

			saveWithLog(uid, womDB);
			
			//update cascade table of work_order
			if (status.intValue() == STATUS_ALLOCATED) {
				WorkOrder woDB = workOrderRepository.findOne(id);
				if (status.intValue() != woDB.getStatus().intValue()) {
					woDB.setStatus(status);
					
					saveWithLog(uid, woDB);
				}
			}
		}
	}
	
	private void saveWithLog(Long uid, WorkOrder workOrder) {
		workOrderRepository.save(workOrder);

		// Add workorderlog
		generateLog(uid, workOrder);		
	}

	private void saveWithLog(Long uid, WorkOrderMaterial workOrderMaterial) {
		workOrderMaterialRepository.save(workOrderMaterial);

		// Add workorderlog
		generateLog(uid, workOrderMaterial);

	}
	
	private boolean chkIfUpdateUpChain(int reqStatus, WorkOrderContainer woc) {
		boolean retVal = false;
		if (reqStatus == STATUS_DREW || reqStatus == STATUS_FEED) {
			retVal = true;
		}
		return retVal;
	}
	
	private boolean chkIfUpdateDownChain(int reqStatus, int originalStatus) {
		boolean retVal = false;
		if (reqStatus == STATUS_APPROVED || reqStatus == STATUS_DOUBLE_CHECKED
				|| reqStatus == STATUS_COMPLETED) {
			retVal = true;
		}
		return retVal;
	}

	/**
	 * update the status with workordermaterials and workordercontainers.
	 * 
	 */
	@Transactional
	public void update(Long uid, WorkOrder workOrder, boolean cascade) {
		if (workOrder.getStatus() == null) {
			throw new PrmRuntimeException("WorkOrder.status is null.");
		}
		Long id = workOrder.getId();
		Integer status = workOrder.getStatus();
		WorkOrder woDB = workOrderRepository.findOne(id);
		if (woDB != null) {
			if (cascade && chkIfUpdateDownChain(status.intValue(), woDB.getStatus().intValue())) {
				List<WorkOrderMaterial> woms = workOrderMaterialRepository
						.findByWid(id);
				for (WorkOrderMaterial wom : woms) {
					Long mid = wom.getMid();
					if (status.intValue() != STATUS_APPROVED) {
						List<WorkOrderContainer> wocs = workOrderContainerRepository
								.findByWidAndMid(id, mid);
						for (WorkOrderContainer woc : wocs) {
							woc.setStatus(status);
	
							saveWithLog(uid, woc);
						}
					}
					wom.setStatus(status);

					saveWithLog(uid, wom);
				}
			}
			
			woDB.setStatus(status);
			
			saveWithLog(uid, woDB);
		}
	}

}
