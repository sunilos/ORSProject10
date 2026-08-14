package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.FacultyDTO;

/**
 * Faculty Service interface.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface FacultyServiceInt extends BaseServiceInt<FacultyDTO> {

	/**
	 * Finds a Faculty by email.
	 *
	 * @param email
	 * @return
	 */
	public FacultyDTO findByEmail(String email, UserContext context);

}
