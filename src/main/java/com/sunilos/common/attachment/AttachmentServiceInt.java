package com.sunilos.common.attachment;

import java.util.List;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;

/**
 * College Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface AttachmentServiceInt extends BaseServiceInt<AttachmentDTO> {

	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, int pageNo, int pageSize,
			UserContext userContext);

	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, UserContext userContext);

}
