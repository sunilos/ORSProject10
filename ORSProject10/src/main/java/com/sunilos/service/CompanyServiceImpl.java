package com.sunilos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.CompanyDAOInt;
import com.sunilos.dto.CompanyDTO;
import com.sunilos.exception.DuplicateRecordException;

/**
 * Session facade of Company Service. It is transactional, apply declarative
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
public class CompanyServiceImpl extends BaseServiceImpl<CompanyDTO, CompanyDAOInt> implements CompanyServiceInt {

	private static Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Transactional(readOnly = true)
	public CompanyDTO findByName(String name, UserContext context) {
		CompanyDTO dto = baseDao.findByUniqueKey("name", name, context);
		return dto;
	}

}
