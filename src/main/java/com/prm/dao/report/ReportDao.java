package com.prm.dao.report;

import java.util.List;

import com.prm.models.report.WorkOrderContainerRpt;

public interface ReportDao {
	
	List<WorkOrderContainerRpt> getWorkOrderContainerRpt(Long wid, Long gid, Integer status);

}
