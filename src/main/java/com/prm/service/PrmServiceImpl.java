package com.prm.service;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prm.dao.work.WorkOrderContainerDao;
import com.prm.dao.work.WorkOrderLogDao;
import com.prm.dao.work.WorkOrderMaterialDao;
import com.prm.exception.PrmInputException;
import com.prm.exception.PrmServerException;
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
	
	private static final int STATUS_CREATED = 0;
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
	
	@Autowired
	private WorkOrderLogDao workOrderLogDao;
	
	
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
		if (workOrder.getWorkStartDate() > workOrder.getWorkEndDate()) {
			throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, "Requested workorder date is not valid.");
		}
		Float quantity = workOrder.getQuantity();
		if (quantity.floatValue() == 0f) {
			throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, "Requested workorder quantity is not set.");
		}
		WorkOrder woDB = workOrderRepository.save(workOrder);
		if (woDB != null) {
			genWorkOrderMaterials(uid, woDB);
			// Add WorkOrderLog
			generateLog(uid, woDB);
		}
		return woDB;
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
				throw new PrmServerException(EXCEPTIONKEY_INVALID_SERVER_DATA,
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
	 * Here support 2 cases:
	 * 
	 * 1. update the status with workorder, workordermaterials and workordercontainers.
	 * 2. update the non-status attributes.
	 */
	@Transactional
	public void update(Long uid, WorkOrder workOrder, boolean cascade) {
		if (uid != null && workOrder.getStatus().intValue() != STATUS_CREATED) {
			updateStatus(uid, workOrder, cascade);
		} else if (uid != null && workOrder.getStatus() == STATUS_CREATED) {
			updateNonStatus(uid, workOrder);
		} else if (uid == null) {
			//status not null
			throw new PrmInputException(EXCEPTIONKEY_PARAMETER_MISSING, "Missing parameter of uid.");
		}
	}

	/**
	 * Allow to update the status of allocated (auto update upstream), 
	 * the status of approved, double_checked and completed (all support auto update downstream).
	 *
	 * workOrderMaterial: includes workOrderMaterialID and status.
	 * Here separated into 2 different transactions. (material and workorder)
	 */
	@Override
	public void update(Long uid, WorkOrderMaterial workOrderMaterial) {
		Integer status = workOrderMaterial.getStatus();
		Long id = workOrderMaterial.getId();
		WorkOrderMaterial womDB = workOrderMaterialRepository.findOne(id);
		updateStatus(uid, womDB, status);
	}
	
	/**
	 * Allow to update the status of allocated (auto update upstream), 
	 * the status of approved, double_checked and completed (all support auto update downstream).
	 *
	 * workOrderMaterial: include status, without workOrderMaterialID.
	 * Here separated into 2 different transactions. (material and workorder)
	 */
	@Override
	public Long update(Long uid, WorkOrderMaterial workOrderMaterial, Long wid,
			Long mid) {
		Long womID = null;
		List<WorkOrderMaterial> woms = workOrderMaterialRepository.findByWidAndMid(wid, mid);
		if (woms != null && woms.size() == 1) {
			WorkOrderMaterial wom = woms.get(0);
			womID = wom.getId();
			updateStatus(uid, wom, workOrderMaterial.getStatus());
		} else {
			throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, "Invalid wid: " + wid + " or mid: " + mid + ".");
		}
		return womID;
	}

	/**
	 * Here separated into 3 different transactions. (container, material and workorder)
	 */
	@Override
	public void update(Long uid, WorkOrderContainer workOrderContainer) {
		Integer status = workOrderContainer.getStatus();
		Long id = workOrderContainer.getId();
		WorkOrderContainer wocDB = workOrderContainerRepository.findOne(id);
		if (wocDB != null) {
			Integer originalStatus = wocDB.getStatus();
			
			chkIfAllowUpdateStatus(status, originalStatus, "workordercontainer");
			
			if (status.intValue() != originalStatus.intValue()) {
				wocDB.setStatus(status);
				
				commitSave(uid, wocDB);

				//check if this is to update the final record.
				if (chkIfUpdateUpChain(status.intValue(), wocDB)) {
					//save workordermaterial
					Long wid = wocDB.getWid();
					Long mid = wocDB.getMid();
					List<WorkOrderMaterial> woms = workOrderMaterialRepository
							.findByWidAndMid(wid, mid);
					if (woms != null && woms.size() == 1) {
						WorkOrderMaterial wom = woms.get(0);
						if (wom != null && status.intValue() != wom.getStatus().intValue()) {
							
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
								throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, "Invalid workorder ID: " + wid);
							}
						}
					} else {
						throw new PrmServerException(EXCEPTIONKEY_INVALID_SERVER_DATA, 
								"WorkOrderMaterial data is not unique or null, wid: " + wid
										+ ", mid: " + mid);
					}
				}				
			}
		}
	}

	/**
	 * Only just created workorder can be deleted.
	 */
	@Transactional
	@Override
	public void delete(Long wid) {
		WorkOrder workOrder = workOrderRepository.findOne(wid);
		Integer status = workOrder.getStatus();
		if (status.intValue() != STATUS_CREATED) {
			throw new PrmInputException(EXCEPTIONKEY_ACTION_NOTALLOWED, "This workorder can not be deleted, as it's status is: " + status);
		}
		workOrderLogDao.delete(wid);
		workOrderContainerDao.delete(wid);
//		workOrderMaterialDao.delete(wid);
		workOrderRepository.delete(wid);
	}

	/**
	 * update the non-status.
	 * 1. update quantity, bomid (this requires to remove the related workordermaterial, and generate it again.)
	 * 2. update other attributes.
	 * @param workOrder
	 */
	private void updateNonStatus(Long uid, WorkOrder workOrder) {
		Long id = workOrder.getId();
		WorkOrder woDB = workOrderRepository.findOne(id);
		if (woDB.getStatus().intValue() > this.STATUS_CREATED) {
			throw new PrmInputException(EXCEPTIONKEY_ACTION_NOTALLOWED, "Not allow to update, as its status is: " + woDB.getStatus());
		}
		if (chkIfNeedUpdateWorkOrderMaterialAndMerge(workOrder, woDB)) {
			//here needs to update the workordermaterial.
			
			workOrderLogDao.delete(id);
			workOrderMaterialDao.delete(id);
			
			workOrderRepository.save(woDB);
			
			genWorkOrderMaterials(uid, woDB);
		} else {
			//no quantity and bomid changes.
			
			workOrderRepository.save(woDB);
		}
	}
	
	private boolean chkIfNeedUpdateWorkOrderMaterialAndMerge(WorkOrder req, WorkOrder woDB) {
		boolean retVal = false;
		// 1. check if any updates on quantity or bomid.
		Float reqQuantity = req.getQuantity();
		Long reqBomId = req.getBid();
		if ((reqQuantity != null && reqQuantity.intValue() != woDB.getQuantity().intValue())
				|| (reqBomId != null && reqBomId.longValue() != woDB.getBid().longValue())) {
			if (reqQuantity != null) {
				woDB.setQuantity(reqQuantity);
			}
			
			if (reqBomId != null) {
				woDB.setBid(reqBomId);
			}
			retVal = true;
		}
		
		
		//2. merge the changes into woDB from request.
		if (req.getFid() != null) {
			woDB.setFid(req.getFid());
		}
		if (req.getLid() != null) {
			woDB.setLid(req.getLid());
		}
		if (req.getPid() != null) {
			woDB.setPid(req.getPid());
		}
		if (req.getSid() != 0) {
			woDB.setSid(req.getSid());
		}
		if (req.getCode() != null) {
			woDB.setCode(req.getCode());
		}
		if (req.getSequence() != null) {
			woDB.setSequence(req.getSequence());
		}
		if (req.getWorkStartDate() != null) {
			woDB.setWorkStartDate(req.getWorkStartDate());
		}		
		if (req.getWorkEndDate() != null) {
			woDB.setWorkEndDate(req.getWorkEndDate());
		}		
		if (req.getOwnerUid() != null) {
			woDB.setOwnerUid(req.getOwnerUid());
		}		
		if (req.getWeighingUid() != null) {
			woDB.setWeighingUid(req.getWeighingUid());
		}		
		if (req.getOperatorUid() != null) {
			woDB.setOperatorUid(req.getOperatorUid());
		}
		
		return retVal;
	}
	
	/**
	 * update the status with workorder, workordermaterials and workordercontainers.
	 * @param uid
	 * @param workOrder
	 * @param cascade
	 */
	private void updateStatus(Long uid, WorkOrder workOrder, boolean cascade) {
		Long id = workOrder.getId();
		Integer status = workOrder.getStatus();
		WorkOrder woDB = workOrderRepository.findOne(id);
		if (woDB != null) {
			chkIfAllowUpdateStatus(status, woDB.getStatus(), "workorder");
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
	
	/**
	 * Allow to update the status of allocated (auto update upstream), 
	 * the status of approved, double_checked and completed (all support auto update downstream).
	 * @param uid
	 * @param womDB
	 * @param newStatus
	 */
	private void updateStatus(Long uid, WorkOrderMaterial womDB, Integer newStatus) {
		if (newStatus == STATUS_CREATED) {
			throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, "Requested WorkOrderMaterial.status is not allowed.");
		}

		if (womDB != null) {
			Integer originalStatus = womDB.getStatus();
			chkIfAllowUpdateStatus(newStatus, originalStatus, "workordermaterial");
			
			if (newStatus == STATUS_DREW || newStatus == STATUS_FEED) {
				throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, "Workordermaterial does not supporte the status: " + newStatus );
			}
			
			womDB.setStatus(newStatus);

			commitSave(uid, womDB);
			
			if (newStatus.intValue() == STATUS_ALLOCATED) {
				//update cascade table of work_order
				if (workOrderMaterialDao.isAllMaterialsStatusAS(womDB.getWid(), newStatus.intValue())) {
					WorkOrder woDB = workOrderRepository.findOne(womDB.getWid());
					if (newStatus.intValue() != woDB.getStatus().intValue()) {
						woDB.setStatus(newStatus);
						
						commitSave(uid, woDB);
					}
				}
			} else if (this.chkIfUpdateDownChain(newStatus, originalStatus)) {
				//update cascade table of workorder_material
				List<WorkOrderContainer> wocs = workOrderContainerRepository
						.findByWidAndMid(womDB.getWid(), womDB.getWid());
				for (WorkOrderContainer woc : wocs) {
					woc.setStatus(newStatus);

					saveWithLog(uid, woc);
				}
			}
		}
		
	}

	
	private void genWorkOrderMaterials(Long uid, WorkOrder woDB) {
		// start with workorder_material
		Float woQty = woDB.getQuantity();
		Bom bom = bomRepository.findOne(woDB.getBid());
		Float bomQty = bom.getQuantity();
		List<BomItem> bomItems = bomItemRepository.findByBid(woDB.getBid());
		for (BomItem bomItem : bomItems) {
			WorkOrderMaterial wom = new WorkOrderMaterial();
			Float biQty = bomItem.getQuantity();
			Float biTorn = bomItem.getTolerance();
			Float qty = biQty * ( woQty / bomQty);
			Float torn = biTorn * ( woQty / bomQty);
			if (logger.isTraceEnabled()) {
				logger.trace("Qty of material: " + bomItem.getMid()
						+ ", for WorkOrder: " + woDB.getId() + " is : " + qty + ", and tolerance is: " + torn);
			}
			wom.setQuantity(qty);
			wom.setTolerance(torn);
			wom.setMid(bomItem.getMid());
			wom.setUnit(bomItem.getUnit());
			wom.setWid(woDB.getId());
			
			WorkOrderMaterial womDB = saveWithLog(uid, wom);
			if (logger.isInfoEnabled()) {
				logger.info("Saved work order material: " + womDB.getId());
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

	private WorkOrderMaterial saveWithLog(Long uid, WorkOrderMaterial workOrderMaterial) {
		WorkOrderMaterial womDB = workOrderMaterialRepository.save(workOrderMaterial);

		// Add workorderlog
		generateLog(uid, workOrderMaterial);
		
		return womDB;
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
		log.setMid(workOrderMaterial.getMid());
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
	
	private void chkIfAllowUpdateStatus(Integer reqStatus, Integer dbStatus, String target) {
		boolean retVal = false;
		
		if (reqStatus != STATUS_COMPLETED) {
			if (reqStatus == dbStatus + 1) {
				retVal = true;
			}
		} else {//change to complete.
			if (dbStatus == STATUS_FEED) {
				retVal = true;
			}
		}
		
		if (retVal == false) {
			throw new PrmInputException(EXCEPTIONKEY_PARAMETER_INVALID, 
					"Status change for " + target + " is not allowed, status in db is: "
							+ dbStatus + " and request status is: " + reqStatus);
		}		
	}
		

}
