
package com.coolmix.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import com.coolmix.service.GsrService;
import com.coolmix.model.Gsr;

@Repository
@Service("gsrService")
@Transactional
public class GsrServiceImpl implements GsrService{
	
	@Autowired
	public JdbcTemplate jdbc;
	//private static final AtomicLong counter = new AtomicLong();     

	public List<Gsr> findAllGsrs() {
		String sql = "SELECT * FROM gsr";
		@SuppressWarnings({ "rawtypes", "unchecked" })
		BeanPropertyRowMapper bprm = new BeanPropertyRowMapper(Gsr.class);
		List<Gsr>gsrList  = jdbc.query(sql, bprm);
		return gsrList;
	}

	public Gsr findById(int id) {
		String sql = "SELECT * FROM gsr WHERE id = ?";
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Gsr gsr = (Gsr)jdbc.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper(Gsr.class));
		return gsr;
	}

}