package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.SubjectDTO;

/**
 * Subject Service interface.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface SubjectServiceInt extends BaseServiceInt<SubjectDTO> {

	/**
	 * Finds a Subject by name.
	 *
	 * @param name
	 * @return
	 */
	public SubjectDTO findByName(String name, UserContext context);

}
