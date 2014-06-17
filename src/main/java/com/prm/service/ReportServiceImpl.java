package com.prm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prm.dao.report.ReportDao;
import com.prm.models.report.WorkOrderContainerRpt;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportDao reportDao;

	public List<WorkOrderContainerRpt> getWorkOrderContainerRpt(Long wid,
			Long gid, Integer status) {
		return reportDao.getWorkOrderContainerRpt(wid, gid, status);
	}

}