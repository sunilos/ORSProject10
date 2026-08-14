package com.sunilos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.FacultyDAOInt;
import com.sunilos.dto.FacultyDTO;
import com.sunilos.exception.DuplicateRecordException;

/**
 * Session facade of Faculty Service. It is transactional, apply declarative
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
public class FacultyServiceImpl extends BaseServiceImpl<FacultyDTO, FacultyDAOInt> implements FacultyServiceInt {

	private static Logger log = LoggerFactory.getLogger(FacultyServiceImpl.class);

	@Transactional(readOnly = true)
	public FacultyDTO findByEmail(String email, UserContext context) {
		return baseDao.findByUniqueKey("email", email, context);
	}

}
