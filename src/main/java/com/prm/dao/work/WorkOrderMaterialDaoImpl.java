package com.prm.dao.work;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.prm.dao.PrmJdbcDao;

@Component
public class WorkOrderMaterialDaoImpl extends PrmJdbcDao implements WorkOrderMaterialDao {
	private Logger logger = LoggerFactory.getLogger(WorkOrderMaterialDaoImpl.class);
	
	@Override
	public boolean isAllMaterialsStatusAS(Long wid, int status) {
		boolean retVal = false;

		Object[] args = {wid};
		int[] argTypes = {java.sql.Types.BIGINT};
		String sql = "select status, count(*) cnt from workorder_material where wid=? group by status ";
		loggerSQL(logger, sql, args);

		List<Map<String, Object>> result = jt.queryForList(sql, args, argTypes);
		if (result != null) {
			if (result.size() == 1) {
				Map<String, Object> oneRec = result.get(0);
				int statusFromDB = (int)oneRec.get("status");
				if (status == statusFromDB) {
					if (logger.isDebugEnabled()) {
						logger.debug("All materials under workorder of " + wid + " are in status of " + status + ".");
					}
					retVal = true;
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("expected status is " + status + ", but the statuts from db is " + statusFromDB + ".");
					}
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("result has " + result.size() + " groups.");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Invalid of query criterial, wid: " + wid + ".");
			}
		}
		
		return retVal;
	}

}
