package com.sunilos.dao;

import static com.sunilos.util.DataValidator.isEmptyString;
import static com.sunilos.util.DataValidator.isZeroNumber;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.dto.DocumentDTO;

@Repository
public class DocumentDAOImpl extends BaseDAOImpl<DocumentDTO> implements DocumentDAOInt {

	@Override
	protected List<Predicate> getWhereClause(DocumentDTO dto, CriteriaBuilder builder, Root<DocumentDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();
		if (dto == null) {
			return whereCondition;
		}

		if (!isEmptyString(dto.getName())) {

			whereCondition.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
		}

		if (!isEmptyString(dto.getDescription())) {

			whereCondition.add(builder.like(qRoot.get("description"), "%" + dto.getDescription() + "%"));
		}

		if (!isEmptyString(dto.getTags())) {

			whereCondition.add(builder.like(qRoot.get("tags"), "%" + dto.getTags() + "%"));
		}

		if (!isEmptyString(dto.getPath())) {

			whereCondition.add(builder.like(qRoot.get("path"), dto.getPath() + "%"));
		}

		if (!isZeroNumber(dto.getUserId())) {

			whereCondition.add(builder.equal(qRoot.get("userId"), dto.getUserId()));
		}

		return whereCondition;
	}

	@Override
	public Class<DocumentDTO> getDTOClass() {
		return DocumentDTO.class;
	}

}