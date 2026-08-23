package com.sunilos.dao;

import static com.sunilos.util.DataValidator.isEmptyString;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.dto.CompanyDTO;

@Repository
public class CompanyDAOImpl extends BaseDAOImpl<CompanyDTO> implements CompanyDAOInt {

	@Override
	protected List<Predicate> getWhereClause(CompanyDTO dto, CriteriaBuilder builder, Root<CompanyDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();
		if (dto == null) {
			return whereCondition;
		}

		if (!isEmptyString(dto.getName())) {

			whereCondition.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
		}

		if (!isEmptyString(dto.getIndustry())) {

			whereCondition.add(builder.like(qRoot.get("industry"), dto.getIndustry() + "%"));
		}

		if (!isEmptyString(dto.getCity())) {

			whereCondition.add(builder.like(qRoot.get("city"), dto.getCity() + "%"));
		}

		return whereCondition;
	}

	@Override
	public Class<CompanyDTO> getDTOClass() {
		return CompanyDTO.class;
	}

}
