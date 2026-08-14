package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.CourseDTO;

/**
 * Course Service interface.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface CourseServiceInt extends BaseServiceInt<CourseDTO> {

	/**
	 * Finds a Course by name.
	 *
	 * @param name
	 * @return
	 */
	public CourseDTO findByName(String name, UserContext context);

}
