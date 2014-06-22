package com.prm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public void create(@RequestParam Long uid, @RequestBody WorkOrder workOrder,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrder saved = prmService.create(uid, workOrder);
		response.setHeader("Location", request.getRequestURL() + "/" + saved.getId());
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
	public void create(@RequestParam Long uid, @RequestBody WorkOrderContainer workOrderContainer,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrderContainer saved = prmService.create(uid, workOrderContainer);
		response.setHeader("Location", request.getRequestURL() + "/" + saved.getId());
	}
	
	/**
	 * Update status of WorkOrder.
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
	public void patch(@RequestParam(required=false) Long uid, @RequestParam(required=false, defaultValue="true") boolean cascade,
			@PathVariable Long id, @RequestBody WorkOrder workOrder,
			HttpServletRequest request, HttpServletResponse response) {
		workOrder.setId(id);
		prmService.update(uid, workOrder, cascade);
		response.setHeader("Location", request.getRequestURL().toString());
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
		response.setHeader("Location", request.getRequestURL().toString());
	}
		
	/**
	 * Update status of WorkOrderMaterial.
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
		response.setHeader("Location", request.getRequestURL().toString());
	}

	/**
	 * Update status of WorkOrderMaterial.
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
		response.setHeader("Location", request.getRequestURL().toString());
	}

	
	/**
	 * Update status of WorkOrderMaterial.
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
		response.setHeader("Location", request.getRequestURL().toString() + "?" + request.getQueryString() );
		List<BomItem> bomItems = prmService.getEstimatedMaterials(bid, quantity);
		Map<String, List<BomItem>> matsMap = new HashMap<String, List<BomItem>>();
		matsMap.put("materials", bomItems);
		Map<String, Map<String, List<BomItem>>> retVal = new HashMap<String, Map<String, List<BomItem>>>();
		retVal.put("_embedded", matsMap);
		return retVal;
	}

}
