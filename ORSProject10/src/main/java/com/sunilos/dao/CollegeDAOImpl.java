package com.sunilos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.dto.CollegeDTO;

@Repository
public class CollegeDAOImpl extends BaseDAOImpl<CollegeDTO> implements CollegeDAOInt {

	@Override
	protected List<Predicate> getWhereClause(CollegeDTO dto, CriteriaBuilder builder, Root<CollegeDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getName())) {

			whereCondition.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
		}

		if (!isEmptyString(dto.getCity())) {

			whereCondition.add(builder.like(qRoot.get("city"), dto.getCity() + "%"));
		}

		if (!isEmptyString(dto.getState())) {

			whereCondition.add(builder.like(qRoot.get("state"), dto.getState() + "%"));
		}

		return whereCondition;
	}

	@Override
	public Class<CollegeDTO> getDTOClass() {
		return CollegeDTO.class;
	}

}
