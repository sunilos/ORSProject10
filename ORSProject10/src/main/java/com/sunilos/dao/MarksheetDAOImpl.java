package com.sunilos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.dto.MarksheetDTO;

@Repository
public class MarksheetDAOImpl extends BaseDAOImpl<MarksheetDTO> implements MarksheetDAOInt {

	@Autowired
	StudentDAOInt studentDao = null;

	@Override
	public List<MarksheetDTO> getMeritList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Predicate> getWhereClause(MarksheetDTO dto, CriteriaBuilder builder, Root<MarksheetDTO> qRoot) {

		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getName())) {

			whereCondition.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
		}

		if (!isEmptyString(dto.getRollNo())) {

			whereCondition.add(builder.like(qRoot.get("rollNo"), dto.getRollNo() + "%"));
		}

		if (!isZeroNumber(dto.getStudentId())) {

			whereCondition.add(builder.equal(qRoot.get("studentId"), dto.getStudentId()));
		}

		return whereCondition;
	}

	@Override
	public Class<MarksheetDTO> getDTOClass() {
		return MarksheetDTO.class;
	}

}
