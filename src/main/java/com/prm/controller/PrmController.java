package com.prm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prm.models.basic.BomItem;
import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderContainer;
import com.prm.models.work.WorkOrderMaterial;
import com.prm.service.PrmService;

@RestController
public class PrmController {
	@Autowired
	PrmService prmService;
	
	/**
	 * Create a new WorkOrder.
	 * 
	 * @param uid
	 * @param workOrder
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workorders", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)	
	public void create(@RequestParam Long uid, @Validated({WorkOrder.Create.class}) @RequestBody WorkOrder workOrder,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrder saved = prmService.create(uid, workOrder);
		response.setHeader("Location",  delLastChar(request.getRequestURL()) + "/" + saved.getId());
	}
	
	/**
	 * Remove a WorkOrder
	 * @param uid
	 * @param workOrder
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workorders", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id, 
			HttpServletRequest request, HttpServletResponse response) {
		prmService.delete(id);
	}
	
	
	/**
	 * Create a new WorkOrderContainer.
	 * 
	 * @param uid
	 * @param workOrderContainer
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workordercontainers", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestParam Long uid, @Valid @RequestBody WorkOrderContainer workOrderContainer,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrderContainer saved = prmService.create(uid, workOrderContainer);
		response.setHeader("Location", delLastChar(request.getRequestURL()) + "/" + saved.getId());
	}
	
	/**
	 * Update WorkOrder:
	 * 1. update status only.
	 * 2. or other attributes.
	 * 
	 * @param uid
	 * @param id
	 * @param workOrderContainer
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workorders/{id}", method = RequestMethod.PATCH)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void patch(@RequestParam Long uid, @RequestParam(required=false, defaultValue="true") boolean cascade,
			@PathVariable Long id, @RequestBody WorkOrder workOrder,
			@RequestParam(required=false, defaultValue="false") boolean nocheck,
			HttpServletRequest request, HttpServletResponse response) {
		workOrder.setId(id);
		prmService.update(uid, workOrder, cascade, nocheck);
	}
	
	/**
	 * Update status of WorkOrderContainer.
	 * 
	 * @param uid
	 * @param id
	 * @param workOrderContainer
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workordercontainers/{id}", method = RequestMethod.PATCH)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void patch(@RequestParam Long uid, @PathVariable Long id,
			@RequestBody WorkOrderContainer workOrderContainer,
			HttpServletRequest request, HttpServletResponse response) {
		workOrderContainer.setId(id);
		prmService.update(uid, workOrderContainer);
	}
		
	/**
	 * 1. Update status of WorkOrderMaterial.
	 * 
	 * Allow to update the status of allocated (auto update upstream), 
	 * the status of approved, double_checked and completed (all support auto update downstream).
	 *
	 * 2. Update the quantity or tolerance of the material in the workorder.
	 *  It will automatically change the workorder's status to Approved, 
	 *  add set the replenish to true to the workorder material. 
	 *  
	 * @param uid
	 * @param id
	 * @param workOrderContainer
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workordermaterials/{id}", method = RequestMethod.PATCH)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void patch(@RequestParam Long uid, @PathVariable Long id,
			@RequestBody WorkOrderMaterial workOrderMaterial,
			HttpServletRequest request, HttpServletResponse response) {
		workOrderMaterial.setId(id);
		prmService.update(uid, workOrderMaterial);
	}

	/**
	 * 1. Update status of WorkOrderMaterial.
	 * 
	 * Allow to update the status of allocated (auto update upstream), 
	 * the status of approved, double_checked and completed (all support auto update downstream).
	 * 
	 * 2. Update the quantity or tolerance of the material in the workorder.
	 *  It will automatically change the workorder's status to Approved, 
	 *  add set the replenish to true to the workorder material. 
	 *  
	 * @param uid
	 * @param id
	 * @param workOrderContainer
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workordermaterials", method = RequestMethod.PATCH)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void patch(@RequestParam Long uid, @RequestParam Long wid, @RequestParam Long mid,
			@RequestBody WorkOrderMaterial workOrderMaterial,
			HttpServletRequest request, HttpServletResponse response) {
		prmService.update(uid, workOrderMaterial, wid, mid);
	}
	
	/**
	 * for adding material to an workorder even the status is feeded.
	 * Change the workorder status back to approved, waiting for allocating.
	 * @param uid
	 * @param workOrderMaterial
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/workordermaterials", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void create(@RequestParam Long uid, 
			@RequestBody WorkOrderMaterial workOrderMaterial,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrderMaterial saved = prmService.create(uid, workOrderMaterial);
		response.setHeader("Location",  delLastChar(request.getRequestURL()) + "/" + saved.getId());
	}
	
	/**
	 * Get the each material quantity base on the bom.
	 * 
	 * @param uid
	 * @param id
	 * @param workOrderContainer
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/bomitems/{bid}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Map<String, List<BomItem>>> get(@RequestParam Float quantity, @PathVariable Long bid,
			HttpServletRequest request, HttpServletResponse response) {
		List<BomItem> bomItems = prmService.getEstimatedMaterials(bid, quantity);
		Map<String, List<BomItem>> matsMap = new HashMap<String, List<BomItem>>();
		matsMap.put("materials", bomItems);
		Map<String, Map<String, List<BomItem>>> retVal = new HashMap<String, Map<String, List<BomItem>>>();
		retVal.put("_embedded", matsMap);
		return retVal;
	}
	
	private StringBuffer delLastChar(StringBuffer sb) {
		if (sb != null) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb;
	}

}
