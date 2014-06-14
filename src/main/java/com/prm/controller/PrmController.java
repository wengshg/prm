package com.prm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prm.models.work.WorkOrder;
import com.prm.models.work.WorkOrderContainer;
import com.prm.service.PrmService;

@RestController
public class PrmController {
	@Autowired
	PrmService workOrderService;

	@RequestMapping(value = "/workorders", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody WorkOrder workOrder,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrder saved = workOrderService.create(workOrder);
		response.setHeader("Location", request.getRequestURI() + "/" + saved.getId());
	}

	@RequestMapping(value = "/workordercontainers", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody WorkOrderContainer workOrderContainer,
			HttpServletRequest request, HttpServletResponse response) {
		WorkOrderContainer saved = workOrderService.create(workOrderContainer);
		response.setHeader("Location", request.getRequestURI() + "/" + saved.getId());
	}
	
	@RequestMapping(value = "/workordercontainers/{id}", method = RequestMethod.PATCH)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void patch(@PathVariable Long id,
			@RequestBody WorkOrderContainer workOrderContainer,
			HttpServletRequest request, HttpServletResponse response) {
		workOrderContainer.setId(id);
		workOrderService.update(workOrderContainer);
		response.setHeader("Location", request.getRequestURI() + "/" + id);
	}

}
