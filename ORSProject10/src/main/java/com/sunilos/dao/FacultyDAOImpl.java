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
import com.sunilos.dto.CollegeDTO;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.FacultyDTO;
import com.sunilos.dto.SubjectDTO;

@Repository
public class FacultyDAOImpl extends BaseDAOImpl<FacultyDTO> implements FacultyDAOInt {

	@Autowired
	CollegeDAOInt collegeDao = null;

	@Autowired
	CourseDAOInt courseDao = null;

	@Autowired
	SubjectDAOInt subjectDao = null;

	@Override
	protected List<Predicate> getWhereClause(FacultyDTO dto, CriteriaBuilder builder, Root<FacultyDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();
		if (dto == null) {
			return whereCondition;
		}

		if (!isEmptyString(dto.getFirstName())) {

			whereCondition.add(builder.like(qRoot.get("firstName"), dto.getFirstName() + "%"));
		}

		if (!isEmptyString(dto.getLastName())) {

			whereCondition.add(builder.like(qRoot.get("lastName"), dto.getLastName() + "%"));
		}

		if (!isEmptyString(dto.getEmail())) {

			whereCondition.add(builder.like(qRoot.get("email"), dto.getEmail() + "%"));
		}

		if (!isZeroNumber(dto.getCollegeId())) {

			whereCondition.add(builder.equal(qRoot.get("collegeId"), dto.getCollegeId()));
		}

		return whereCondition;
	}

	@Override
	public void populate(FacultyDTO dto, UserContext userContext) {
		CollegeDTO collegeDTO = collegeDao.findByPK(dto.getCollegeId(), userContext);
		if (collegeDTO != null) {
			dto.setCollegeName(collegeDTO.getName());
		}

		CourseDTO courseDTO = courseDao.findByPK(dto.getCourseId(), userContext);
		if (courseDTO != null) {
			dto.setCourseName(courseDTO.getName());
		}

		SubjectDTO subjectDTO = subjectDao.findByPK(dto.getSubjectId(), userContext);
		if (subjectDTO != null) {
			dto.setSubjectName(subjectDTO.getName());
		}
	}

	@Override
	public Class<FacultyDTO> getDTOClass() {
		return FacultyDTO.class;
	}

}
