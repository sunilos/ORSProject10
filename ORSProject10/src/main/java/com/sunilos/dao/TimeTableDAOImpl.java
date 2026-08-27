package com.sunilos.dao;

import static com.sunilos.util.DataValidator.isEmptyString;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.dto.TimeTableDTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class TimeTableDAOImpl extends BaseDAOImpl<TimeTableDTO> implements TimeTableDAOInt {

	@Autowired
	CourseDAOInt courseDao = null;

	@Autowired
	SubjectDAOInt subjectDao = null;

	@Override
	public Class<TimeTableDTO> getDTOClass() {
		return TimeTableDTO.class;
	}

	@Override
	protected List<Predicate> getWhereClause(TimeTableDTO dto, CriteriaBuilder builder, Root<TimeTableDTO> qRoot) {

		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (dto == null) {
			return whereCondition;
		}

		// Semester Search
		if (!isEmptyString(dto.getSemester())) {
			whereCondition.add(builder.like(qRoot.get("semester"), dto.getSemester() + "%"));
		}

		// Course Search
		if (dto.getCourseId() != null) {
			whereCondition.add(builder.equal(qRoot.get("courseId"), dto.getCourseId()));
		}

		// Subject Search
		if (dto.getSubjectId() != null) {
			whereCondition.add(builder.equal(qRoot.get("subjectId"), dto.getSubjectId()));
		}

		// Exam Time Search
		if (!isEmptyString(dto.getExamTime())) {
			whereCondition.add(builder.like(qRoot.get("examTime"), dto.getExamTime() + "%"));
		}

		return whereCondition;
	}

	@Override
	public void populate(TimeTableDTO dto, UserContext userContext) {

		CourseDTO courseDTO = courseDao.findByPK(dto.getCourseId(), userContext);

		if (courseDTO != null) {
			dto.setCourseName(courseDTO.getName());
		}

		SubjectDTO subjectDTO = subjectDao.findByPK(dto.getSubjectId(), userContext);

		if (subjectDTO != null) {
			dto.setSubjectName(subjectDTO.getName());
		}
	}

}