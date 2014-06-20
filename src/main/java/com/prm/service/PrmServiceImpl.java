package com.prm.service;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prm.Exception.PrmRuntimeException;
import com.prm.dao.work.WorkOrderContainerDao;
import com.prm.dao.work.WorkOrderMaterialDao;
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
	private WorkOrderRepository workOrderRepository;

	@Autowired
	private BomRepository bomRepository;

	@Autowired
	private BomItemRepository bomItemRepository;

	@Autowired
	private WorkOrderMaterialRepository workOrderMaterialRepository;

	@Autowired
	private WorkOrderContainerRepository workOrderContainerRepository;

	@Autowired
	private WorkOrderLogRepository workOrderLogRepository;
	
	@Autowired
	private WorkOrderContainerDao workOrderContainerDao;
	
	@Autowired
	private WorkOrderMaterialDao workOrderMaterialDao;
	
	
	/**
	 * get the virtual workorder_material (estimate).
	 */
	public List<BomItem> getEstimatedMaterials(Long bid, Float quantity) {
		Bom bom = bomRepository.findOne(bid);
		Float bomQty = bom.getQuantity();
		List<BomItem> bomItems = bomItemRepository.findByBid(bid);
		for (BomItem bomItem : bomItems) {
			Float biQty = bomItem.getQuantity();
			Float qty = quantity * (biQty / bomQty);
			bomItem.setQuantity(qty);
		}
		
		return bomItems;
	}

	/**
	 * Generate both tables of workorder and workorder_material
	 */
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
				Float biTorn = bomItem.getTolerance();
				Float qty = biQty * ( woQty / bomQty);
				Float torn = biTorn * ( woQty / bomQty);
				if (logger.isTraceEnabled()) {
					logger.trace("Qty of material: " + bomItem.getMid()
							+ ", for WorkOrder: " + swo.getId() + "is : " + qty);
				}
				wom.setQuantity(qty);
				wom.setTolerance(torn);
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

	/**
	 * update the status with workorder, workordermaterials and workordercontainers.
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
//				if (status.intValue() <= woDB.getStatus().intValue()) {
//					throw new PrmRuntimeException("Status is not allowed to update for this workorder.");
//				}
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

	/**
	 * Here separated into 2 different transactions. (material and workorder)
	 */
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

			commitSave(uid, womDB);
			
			//update cascade table of work_order
			if (status.intValue() == STATUS_ALLOCATED) {
				if (workOrderMaterialDao.isAllMaterialsStatusAS(womDB.getWid(), status.intValue())) {
					WorkOrder woDB = workOrderRepository.findOne(id);
					if (status.intValue() != woDB.getStatus().intValue()) {
						woDB.setStatus(status);
						
						commitSave(uid, woDB);
					}
				}
			}
		}
	}
		
	/**
	 * Here separated into 3 different transactions. (container, material and workorder)
	 */
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
			if (status.intValue() != originalStatus.intValue()) {
				wocDB.setStatus(status);
				
				commitSave(uid, wocDB);

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
							
							commitSave(uid, wom);
						}
						if (workOrderMaterialDao.isAllMaterialsStatusAS(wid, status)) {
							//save workorder
							WorkOrder workOrder = workOrderRepository.findOne(wid);
							if (workOrder != null) {
								if (status.intValue() != workOrder.getStatus().intValue()) {
									workOrder.setStatus(status);
									
									commitSave(uid, workOrder);
								}
							} else {
								throw new PrmRuntimeException("Invalid workorder ID: " + wid);
							}
						}
					} else {
						throw new PrmRuntimeException(
								"WorkOrderMaterial data is not unique or null, wid: " + wid
										+ ", mid: " + mid);
					}
				}				
			}
		}
	}

	
	private boolean chkIfUpdateUpChain(int reqStatus, WorkOrderContainer woc) {
		boolean retVal = false;
		if (reqStatus == STATUS_DREW || reqStatus == STATUS_FEED) {
			Long wid = woc.getWid();
			Long mid = woc.getMid();
			if (workOrderContainerDao.isAllContainersStatusAs(wid, mid, reqStatus)) {
				retVal = true;
			}
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
	
	
	
	
	
	@Transactional
	private void commitSave(Long uid, WorkOrder workOrder) {
		saveWithLog(uid, workOrder);
	}
	
	@Transactional
	private void commitSave(Long uid, WorkOrderMaterial workOrderMaterial) {
		saveWithLog(uid, workOrderMaterial);
	}
	
	@Transactional
	private void commitSave(Long uid, WorkOrderContainer workOrderContainer) {
		saveWithLog(uid, workOrderContainer);
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
	
	private void saveWithLog(Long uid, WorkOrderContainer workOrderContainer) {
		workOrderContainerRepository.save(workOrderContainer);

		// Add workorderlog
		generateLog(uid, workOrderContainer);
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
		

}
