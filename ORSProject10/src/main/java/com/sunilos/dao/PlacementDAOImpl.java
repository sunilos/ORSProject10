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
import com.sunilos.dto.CompanyDTO;
import com.sunilos.dto.PlacementDTO;
import com.sunilos.dto.StudentDTO;

@Repository
public class PlacementDAOImpl extends BaseDAOImpl<PlacementDTO> implements PlacementDAOInt {

	@Autowired
	StudentDAOInt studentDao = null;

	@Autowired
	CollegeDAOInt collegeDao = null;

	@Autowired
	CompanyDAOInt companyDao = null;

	@Override
	protected List<Predicate> getWhereClause(PlacementDTO dto, CriteriaBuilder builder, Root<PlacementDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();
		if (dto == null) {
			return whereCondition;
		}

		if (!isZeroNumber(dto.getStudentId())) {

			whereCondition.add(builder.equal(qRoot.get("studentId"), dto.getStudentId()));
		}

		if (!isZeroNumber(dto.getCollegeId())) {

			whereCondition.add(builder.equal(qRoot.get("collegeId"), dto.getCollegeId()));
		}

		if (!isZeroNumber(dto.getCompanyId())) {

			whereCondition.add(builder.equal(qRoot.get("companyId"), dto.getCompanyId()));
		}

		if (!isEmptyString(dto.getStatus())) {

			whereCondition.add(builder.equal(qRoot.get("status"), dto.getStatus()));
		}

		if (!isEmptyString(dto.getJobType())) {

			whereCondition.add(builder.equal(qRoot.get("jobType"), dto.getJobType()));
		}

		return whereCondition;
	}

	@Override
	public void populate(PlacementDTO dto, UserContext userContext) {

		StudentDTO studentDTO = studentDao.findByPK(dto.getStudentId(), userContext);
		if (studentDTO != null) {
			dto.setStudentName(studentDTO.getFirstName() + " " + studentDTO.getLastName());
		}

		CollegeDTO collegeDTO = collegeDao.findByPK(dto.getCollegeId(), userContext);
		if (collegeDTO != null) {
			dto.setCollegeName(collegeDTO.getName());
		}

		CompanyDTO companyDTO = companyDao.findByPK(dto.getCompanyId(), userContext);
		if (companyDTO != null) {
			dto.setCompanyName(companyDTO.getName());
		}
	}

	@Override
	public Class<PlacementDTO> getDTOClass() {
		return PlacementDTO.class;
	}

}
