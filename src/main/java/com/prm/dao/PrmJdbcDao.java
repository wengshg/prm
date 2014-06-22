package com.prm.dao;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class PrmJdbcDao {
	@Autowired
	protected JdbcTemplate jt;

	protected void loggerSQL(Logger logger, String sql, Object[] params) {
		if (logger.isDebugEnabled()) {
			logger.debug("Sql: " + sql);
			if (params != null) {
				logger.debug("Sql Parameters: ");
				for (Object obj : params) {
					logger.debug(obj.toString());
				}
			}
		}
	}

}
