package com.prm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prm.models.report.WorkOrderContainerRpt;
import com.prm.service.ReportService;

@RestController
public class ReportController {
	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/report/workordercontainer", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Map<String, List<WorkOrderContainerRpt>>> create(
			@RequestParam Long wid, @RequestParam Long gid,
			@RequestParam(required=false) Integer status, HttpServletRequest request,
			HttpServletResponse response) {
		List<WorkOrderContainerRpt> rpts = reportService
				.getWorkOrderContainerRpt(wid, gid, status);
		response.setHeader("Location", request.getRequestURL().toString() + "?" + request.getQueryString());
		Map<String, List<WorkOrderContainerRpt>> rptMap = new HashMap<String, List<WorkOrderContainerRpt>>();
		rptMap.put("workordercontainerRpt", rpts);
		Map<String, Map<String, List<WorkOrderContainerRpt>>> retVal = new HashMap<String, Map<String, List<WorkOrderContainerRpt>>>();
		retVal.put("_embedded", rptMap);
		return retVal;
	}

}
