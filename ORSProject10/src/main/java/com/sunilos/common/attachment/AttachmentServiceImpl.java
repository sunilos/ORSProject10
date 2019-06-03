package com.sunilos.common.attachment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;

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
public class AttachmentServiceImpl extends BaseServiceImpl<AttachmentDTO, AttachmentDAOInt>
		implements AttachmentServiceInt {

	private static Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

	@Transactional(readOnly = true)
	public AttachmentDTO findByName(String name, UserContext userContext) {
		return baseDao.findByUniqueKey("name", name, userContext);
	}

	@Override
	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, int pageNo, int pageSize,
			UserContext userContext) {
		return baseDao.search(dto, pageNo, pageSize, userContext);
	}

	@Override
	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, UserContext userContext) {
		return baseDao.search(dto, userContext);
	}

}
