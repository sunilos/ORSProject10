package com.sunilos.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.TimeTableDAOInt;
import com.sunilos.dto.TimeTableDTO;

/**
 * Session facade of TimeTable Service. It is transactional, apply declarative
 * transactions with help of Spring AOP.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@Service
@Transactional
public class TimeTableServiceImpl extends BaseServiceImpl<TimeTableDTO, TimeTableDAOInt>
		implements TimeTableServiceInt {

	private static Logger log = LoggerFactory.getLogger(TimeTableServiceImpl.class);

	@Override
	@Transactional(readOnly = true)
	public TimeTableDTO findByCourseSubjectSemester(Long courseId, Long subjectId, String semester,
			UserContext userContext) {

		TimeTableDTO dto = new TimeTableDTO();

		dto.setCourseId(courseId);
		dto.setSubjectId(subjectId);
		dto.setSemester(semester);

		// Tumhare BaseDAOImpl ke according
		List<TimeTableDTO> list = baseDao.findAll(dto, userContext);

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

}