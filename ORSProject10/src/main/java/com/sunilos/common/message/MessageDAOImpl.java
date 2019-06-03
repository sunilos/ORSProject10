package com.sunilos.common.message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;

@Repository
public class MessageDAOImpl extends BaseDAOImpl<MessageDTO> implements MessageDAOInt {

	@Override
	public Class<MessageDTO> getDTOClass() {
		return MessageDTO.class;
	}

	@Override
	protected List<Predicate> getWhereClause(MessageDTO dto, CriteriaBuilder builder, Root<MessageDTO> qRoot) {
		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getSubject())) {

			whereCondition.add(builder.like(qRoot.get("subject"), dto.getSubject() + "%"));
		}

		if (!isEmptyString(dto.getBody())) {
			whereCondition.add(builder.like(qRoot.get("body"), "%" + dto.getBody() + "%"));
		}

		if (!isEmptyString(dto.getStatus())) {
			whereCondition.add(builder.equal(qRoot.get("status"), dto.getStatus()));
		}

		if (!isEmptyString(dto.getCode())) {
			whereCondition.add(builder.equal(qRoot.get("code"), dto.getCode()));
		}

		if (!isEmptyString(dto.getType())) {
			whereCondition.add(builder.equal(qRoot.get("type"), dto.getType()));
		}

		if (dto.getHtml() != null) {
			whereCondition.add(builder.equal(qRoot.get("html"), dto.getHtml()));
		}

		return whereCondition;
	}

}
