package com.sunilos.dao;

import static com.sunilos.util.DataValidator.isEmptyString;
import static com.sunilos.util.DataValidator.isZeroNumber;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.SubjectDTO;

@Repository
public class SubjectDAOImpl extends BaseDAOImpl<SubjectDTO> implements SubjectDAOInt {

	@Autowired
	CourseDAOInt courseDao = null;

	@Override
	public List<Predicate> getWhereClause(SubjectDTO dto, CriteriaBuilder builder, Root<SubjectDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getName())) {

			whereCondition.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
		}

		if (!isZeroNumber(dto.getCourseId())) {

			whereCondition.add(builder.equal(qRoot.get("courseId"), dto.getCourseId()));
		}

		return whereCondition;
	}

	@Override
	public void populate(SubjectDTO dto, UserContext userContext) {
		CourseDTO courseDTO = courseDao.findByPK(dto.getCourseId(), userContext);
		if (courseDTO != null) {
			dto.setCourseName(courseDTO.getName());
		}
	}

	@Override
	public Class<SubjectDTO> getDTOClass() {
		return SubjectDTO.class;
	}

}
