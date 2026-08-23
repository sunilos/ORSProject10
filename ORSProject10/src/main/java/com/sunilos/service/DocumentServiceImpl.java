package com.sunilos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.dao.DocumentDAOInt;
import com.sunilos.dto.DocumentDTO;

/**
 * Session facade of Document Service. It is transactional, apply declarative
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
public class DocumentServiceImpl extends BaseServiceImpl<DocumentDTO, DocumentDAOInt> implements DocumentServiceInt {
}