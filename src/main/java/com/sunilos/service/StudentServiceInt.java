package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.StudentDTO;

/**
 * College Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface StudentServiceInt extends BaseServiceInt<StudentDTO> {

	/**
	 * Finds a Role by name.
	 * 
	 * @param roleName
	 * @return
	 */
	public StudentDTO findByEmail(String email, UserContext context);

}