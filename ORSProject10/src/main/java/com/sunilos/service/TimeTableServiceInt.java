package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.TimeTableDTO;

/**
 * TimeTable Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface TimeTableServiceInt extends BaseServiceInt<TimeTableDTO> {

	/**
	 * Finds TimeTable by course, subject, semester and exam date.
	 * 
	 * @param courseId
	 * @param subjectId
	 * @param semester
	 * @param userContext
	 * @return
	 */
	public TimeTableDTO findByCourseSubjectSemester(Long courseId, Long subjectId, String semester,
			UserContext userContext);

}