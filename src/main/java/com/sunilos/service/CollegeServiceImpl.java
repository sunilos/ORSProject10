package com.sunilos.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.CollegeDAOInt;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.exception.DuplicateRecordException;

/**
 * Session facade of Role Service. It is transactional, apply declarative
 * transactions with help of Spring AOP.
 * 
 * If unchecked exception is propagated from a method then transaction is rolled
 * back.
 * 
 * Default propagation value is Propagation.REQUIRED and readOnly = false
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@Service
@Transactional
public class CollegeServiceImpl extends BaseServiceImpl<CollegeDTO, CollegeDAOInt> implements CollegeServiceInt {

	private static Logger log = Logger.getLogger(CollegeServiceImpl.class);

	@Transactional(readOnly = true)
	public CollegeDTO findByName(String name, UserContext context) {
		CollegeDTO dto = baseDao.findByUniqueKey("name", name, context);
		return dto;
	}

}