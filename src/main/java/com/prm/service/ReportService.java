package com.prm.service;

import java.util.List;

import com.prm.models.report.WorkOrderContainerRpt;

public interface ReportService {
	
	List<WorkOrderContainerRpt> getWorkOrderContainerRpt(Long wid, Long gid, Integer status);
}
