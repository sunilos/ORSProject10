package com.sunilos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.StudentDAOInt;
import com.sunilos.dto.StudentDTO;
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
public class StudentServiceImpl extends BaseServiceImpl<StudentDTO, StudentDAOInt> implements StudentServiceInt {

	private static Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Transactional(readOnly = true)
	public StudentDTO findByEmail(String email, UserContext context) {
		return baseDao.findByUniqueKey("email", email, context);
	}

}