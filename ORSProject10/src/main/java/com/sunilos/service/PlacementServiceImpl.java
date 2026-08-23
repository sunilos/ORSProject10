package com.sunilos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.dao.PlacementDAOInt;
import com.sunilos.dto.PlacementDTO;

/**
 * Session facade of Placement Service. It is transactional, apply declarative
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
public class PlacementServiceImpl extends BaseServiceImpl<PlacementDTO, PlacementDAOInt>
		implements PlacementServiceInt {

}
