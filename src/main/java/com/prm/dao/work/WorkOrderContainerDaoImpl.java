package com.prm.dao.work;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.prm.dao.PrmJdbcDao;
import com.prm.models.report.WorkOrderContainerRpt;

@Component
public class WorkOrderContainerDaoImpl extends PrmJdbcDao implements WorkOrderContainerDao {

	private Logger logger = LoggerFactory.getLogger(WorkOrderContainerDaoImpl.class);
	
	@Override
	public boolean isAllContainersStatusAs(Long wid, Long mid, Integer status) {
		boolean retVal = false;
		
		Object[] args = {wid, mid, status};
		int[] argTypes = {java.sql.Types.BIGINT, java.sql.Types.BIGINT, java.sql.Types.INTEGER};
		String sql = "select count(*) cnt from workorder_container where wid=? and mid=? and status!=? ";
		loggerSQL(logger, sql, args);
		
		Integer result = jt.queryForObject(sql, args, argTypes, 
				new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer result = 0;
				if (rs != null) {
					result = new Integer(rs.getInt("cnt"));
				}
				return result; 
			}
		});
		
		if (logger.isDebugEnabled()) {
			logger.debug("sql result: " + result);
		}
		
		if (result.intValue() == 0) {
			retVal = true;
		}
		
		return retVal;
	}

	@Override
	public void delete(Long wid) {
		// TODO Auto-generated method stub
		
	}

}
