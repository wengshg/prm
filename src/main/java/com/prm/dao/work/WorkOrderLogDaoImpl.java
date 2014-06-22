package com.prm.dao.work;

import com.prm.dao.PrmJdbcDao;

public class WorkOrderLogDaoImpl extends PrmJdbcDao implements WorkOrderLogDao {

	@Override
	public void delete(Long wid) {
		jt.update("delete from workorder_log where wid=?", wid);
	}

}
