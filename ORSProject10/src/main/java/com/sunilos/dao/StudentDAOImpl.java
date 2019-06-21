package com.sunilos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.dto.StudentDTO;

@Repository
public class StudentDAOImpl extends BaseDAOImpl<StudentDTO> implements StudentDAOInt {

	@Override
	protected List<Predicate> getWhereClause(StudentDTO dto, CriteriaBuilder builder, Root<StudentDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getFirstName())) {

			whereCondition.add(builder.like(qRoot.get("firstName"), dto.getFirstName() + "%"));
		}

		if (!isEmptyString(dto.getLastName())) {

			whereCondition.add(builder.like(qRoot.get("lastName"), dto.getLastName() + "%"));
		}

		if (!isEmptyString(dto.getEmail())) {

			whereCondition.add(builder.like(qRoot.get("email"), dto.getEmail() + "%"));
		}

		if (isNotNull(dto.getDob())) {

			whereCondition.add(builder.equal(qRoot.get("dob"), dto.getDob()));
		}

		if (!isZeroNumber(dto.getCollegeId())) {
			whereCondition.add(builder.equal(qRoot.get("collegeId"), dto.getCollegeId()));
		}

		return whereCondition;
	}

	@Override
	public Class<StudentDTO> getDTOClass() {
		return StudentDTO.class;
	}

	@Autowired
	CollegeDAOInt collegeService = null;

	@Override
	protected void populate(StudentDTO dto, UserContext userContext) {
		CollegeDTO collegeDTO = collegeService.findByPK(dto.getCollegeId(), userContext);
		if (collegeDTO != null) {
			dto.setCollegeName(collegeDTO.getName());
		}
	}

}
