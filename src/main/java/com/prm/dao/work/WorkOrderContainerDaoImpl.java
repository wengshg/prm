package com.prm.dao.work;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.prm.dao.PrmJdbcDao;

@Component
public class WorkOrderContainerDaoImpl extends PrmJdbcDao implements WorkOrderContainerDao {

	private Logger logger = LoggerFactory.getLogger(WorkOrderContainerDaoImpl.class);
	
	@Override
	public boolean isAllContainersStatusAs(Long wid, Long mid, int status) {
		boolean retVal = false;
		
		Object[] args = {wid, mid};
		int[] argTypes = {java.sql.Types.BIGINT, java.sql.Types.BIGINT};
		String sql = "select status, count(*) cnt from workorder_container where wid=? and mid=? group by status ";
		loggerSQL(logger, sql, args);
		
		List<Map<String, Object>> result = jt.queryForList(sql, args, argTypes);
		if (result != null) {
			if (result.size() == 1) {
				Map<String, Object> oneRec = result.get(0);
				int statusFromDB = (int)oneRec.get("status");
				if (status == statusFromDB) {
					if (logger.isDebugEnabled()) {
						logger.debug("All containers under workorder material of wid: " + wid + ", mid: " + mid + " are in status of " + status + ".");
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
				logger.debug("Invalid of query criterial, wid: " + wid + ", mid: " + mid + ".");
			}
		}
		
		return retVal;
	}

}
