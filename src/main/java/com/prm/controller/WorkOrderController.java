package com.prm.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderMaterial;
import com.prm.resources.work.WorkOrderMaterialRepository;
import com.prm.service.WorkOrderService;

@RestController
public class WorkOrderController {
	@Autowired
	WorkOrderService workOrderService;
	
	@Autowired
	WorkOrderMaterialRepository womRepo;
	
	@RequestMapping(value="/workorders", method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus( HttpStatus.CREATED )
	public void create(@RequestBody WorkOrder workOrder, HttpServletResponse  response) {
		WorkOrder saved = workOrderService.create(workOrder);
		response.setHeader("Location",  "/" + saved.getId());
	}
	
	@RequestMapping(value="/workordermaterials", method=RequestMethod.GET)
	@ResponseBody
	@ResponseStatus( HttpStatus.FOUND )
	public List<WorkOrderMaterial> get(@RequestParam("wid") long wid, @RequestParam("mid") long mid, HttpServletResponse  response) {
		return womRepo.findByWidAndMid(wid, mid);
	}
	
}
