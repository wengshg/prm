package com.prm.dao.work;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.prm.dao.PrmJdbcDao;

@Component
public class WorkOrderMaterialDaoImpl extends PrmJdbcDao implements WorkOrderMaterialDao {
	private Logger logger = LoggerFactory.getLogger(WorkOrderMaterialDaoImpl.class);
	
	@Override
	public boolean isAllMaterialsStatusAS(Long wid, Integer status) {
		boolean retVal = false;

		Object[] args = {wid, status};
		int[] argTypes = {java.sql.Types.BIGINT, java.sql.Types.INTEGER};
		String sql = "select count(*) cnt from workorder_material where wid=? and status!=? ";
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

		if (result.intValue() == 0) {
			retVal = true;
		}
		
		return retVal;
	}

	@Override
	public void delete(Long wid) {
		jt.update("delete from workorder_material where wid=?", wid);
		
	}
	

}
