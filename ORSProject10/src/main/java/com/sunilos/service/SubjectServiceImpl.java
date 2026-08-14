package com.sunilos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.SubjectDAOInt;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.exception.DuplicateRecordException;

/**
 * Session facade of Subject Service. It is transactional, apply declarative
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
public class SubjectServiceImpl extends BaseServiceImpl<SubjectDTO, SubjectDAOInt> implements SubjectServiceInt {

	private static Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

	@Transactional(readOnly = true)
	public SubjectDTO findByName(String name, UserContext context) {
		SubjectDTO dto = baseDao.findByUniqueKey("name", name, context);
		return dto;
	}

}
