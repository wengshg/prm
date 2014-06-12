package com.prm.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.prm.models.work.WorkOrder;
import com.prm.service.WorkOrderService;

@RestController
@RequestMapping("/workorders")
public class WorkOrderController {
	@Autowired
	WorkOrderService workOrderService;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus( HttpStatus.CREATED )
	public void create(@RequestBody WorkOrder workOrder, HttpServletResponse  response) {
		WorkOrder saved = workOrderService.create(workOrder);
		response.setHeader("Location",  "/" + saved.getId());
	}
}
