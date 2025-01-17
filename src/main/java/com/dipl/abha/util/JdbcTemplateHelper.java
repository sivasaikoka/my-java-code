package com.dipl.abha.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.dipl.abha.dto.AbhaRegistrationDTO;
import com.dipl.abha.dto.TenanIdAndIsIntegratedModule;

@Service
public class JdbcTemplateHelper {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public <T> List<T> getResults(String query, Class<T> class1) {
		try {
			return (List<T>) jdbcTemplate.query(query, new BeanPropertyRowMapper<>(class1));
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}

	public Object getAbhaRegistration(String query) {
		try {
			return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(AbhaRegistrationDTO.class));
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}

	public String getTenantUrl(String query) {
		try {
			return jdbcTemplate.queryForObject(query, String.class);
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}

	public Long getPatientId(String query) {
		try {
			return jdbcTemplate.queryForObject(query, Long.class);
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}

	public int update(String query) {
		return jdbcTemplate.update(query);
	}

	public Object getTenantUrlIsIntegratedModule(String query) {
		try {
			return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(TenanIdAndIsIntegratedModule.class));
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}

	public  <T> List<T> getHrpDoctorDetails(String query,Class<T> class1) {
		try {
			return (List<T>)  jdbcTemplate.queryForObject(query,new BeanPropertyRowMapper<>(class1));
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}
	
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
	    return jdbcTemplate.queryForObject(sql, args, rowMapper);
	}

}