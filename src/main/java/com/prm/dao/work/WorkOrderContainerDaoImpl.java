package com.prm.dao.work;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.prm.dao.PrmJdbcDao;

public class WorkOrderContainerDaoImpl extends PrmJdbcDao implements WorkOrderContainerDao {

	@Override
	public boolean isAllContainersStatusAs(Long wid, Long mid, Integer status) {
		
		return false;
	}

}
