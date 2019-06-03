package com.sunilos.common.attachment;

import java.util.List;

import com.sunilos.common.BaseDAOInt;
import com.sunilos.common.UserContext;

/**
 * Role DAO interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public interface AttachmentDAOInt extends BaseDAOInt<AttachmentDTO> {

	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, int pageNo, int pageSize, UserContext userContext);

	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, UserContext userContext);

}
