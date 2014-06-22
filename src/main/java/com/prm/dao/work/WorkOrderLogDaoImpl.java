package com.prm.dao.work;

import org.springframework.stereotype.Component;

import com.prm.dao.PrmJdbcDao;
@Component
public class WorkOrderLogDaoImpl extends PrmJdbcDao implements WorkOrderLogDao {

	@Override
	public void delete(Long wid) {
		jt.update("delete from workorder_log where wid=?", wid);
	}

}
