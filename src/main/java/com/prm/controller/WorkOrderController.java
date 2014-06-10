package com.prm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prm.models.work.WorkOrder;
import com.prm.service.WorkOrderService;

@RestController
@RequestMapping("/workorders")
public class WorkOrderController {
	@Autowired
	WorkOrderService workOrderService;
	
	@RequestMapping(method=RequestMethod.POST)
	public WorkOrder create(@RequestBody WorkOrder workOrder) {
		return workOrderService.create(workOrder);
	}
}
