package com.prm.dao.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.prm.models.report.WorkOrderContainerRpt;

@Component
public class ReportDaoImpl implements ReportDao {

	private Logger logger = LoggerFactory.getLogger(ReportDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jt;
	
	public List<WorkOrderContainerRpt> getWorkOrderContainerRpt(Long wid, Long gid, Integer status) {
		Object[] params = null;
		StringBuilder sql = new StringBuilder("select mid, count(1) as cnt, sum(total) as sumTotal, sum(quantity) as sumQuantity ");
		sql.append("from workorder_container where wid=? and gid=? ");
		if (status != null) {
			sql.append("and status=? ");
			params = new Object[3];
			params[0] = wid;
			params[1] = gid;
			params[2] = status;
		} else {
			params = new Object[2];
			params[0] = wid;
			params[1] = gid;
		}
		sql.append("group by mid order by mid ");
		String sqlString = sql.toString();
		
		loggerSQL(sqlString, params);
		
		List<WorkOrderContainerRpt> rpts = jt.query(sqlString, params,
				new RowMapper<WorkOrderContainerRpt>() {
			public WorkOrderContainerRpt mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorkOrderContainerRpt rpt = new WorkOrderContainerRpt();
				rpt.setMid(rs.getLong("mid"));
				rpt.setCount(rs.getInt("cnt"));
				rpt.setSumTotal(rs.getFloat("sumTotal"));
				rpt.setSumQuantity(rs.getFloat("sumQuantity"));
				return rpt;
			}
		});
		return rpts;
	}

	private void loggerSQL(String sql, Object[] params) {
		if (logger.isDebugEnabled()) {
			logger.debug("Sql of getWorkOrderContainerRpt: " + sql);
			if (params != null) {
				logger.debug("Sql Parameters: ");
				for (Object obj : params) {
					logger.debug(obj.toString());
				}
			}
		}
	}

}
