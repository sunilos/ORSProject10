package com.sunilos.common.attachment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.common.UserContext;

@Repository
public class AttachmentDAOImpl extends BaseDAOImpl<AttachmentDTO> implements AttachmentDAOInt {

	@Override
	public Class<AttachmentDTO> getDTOClass() {
		return AttachmentDTO.class;
	}

	@Override
	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, int pageNo, int pageSize,
			UserContext userContext) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Create criteria
		CriteriaQuery<AttachmentSummaryDTO> cq = builder.createQuery(AttachmentSummaryDTO.class);

		// Columns information
		Root<AttachmentSummaryDTO> qRoot = cq.from(AttachmentSummaryDTO.class);

		// Column of query
		cq.select(qRoot);

		// Create where conditions
		List<Predicate> whereClause = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getName())) {

			whereClause.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
		}

		if (!isEmptyString(dto.getDescription())) {

			whereClause.add(builder.like(qRoot.get("description"), "%" + dto.getDescription() + "%"));
		}

		if (!isEmptyString(dto.getTags())) {

			whereClause.add(builder.like(qRoot.get("tags"), "%" + dto.getTags() + "%"));
		}

		if (!isEmptyString(dto.getPath())) {

			whereClause.add(builder.like(qRoot.get("path"), dto.getPath() + "%"));
		}

		if (!isZeroNumber(dto.getUserId())) {

			whereClause.add(builder.equal(qRoot.get("userId"), dto.getUserId()));
		}

		if (!isZeroNumber(dto.getId())) {
			
			whereClause.add(builder.equal(qRoot.get("id"), dto.getId()));
		}

		// Put organization filter
		if (dto.isGroupFilter()) {
			whereClause.add(builder.equal(qRoot.get("orgId"), dto.getOrgId()));
		}

		System.out.println(  dto.getName() +  " Where conditions " + whereClause.size() );
		cq.where(whereClause.toArray(new Predicate[whereClause.size()]));

		cq.orderBy(builder.asc(qRoot.get("name")));

		TypedQuery<AttachmentSummaryDTO> query = entityManager.createQuery(cq);

		if (pageSize > 0) {
			query.setFirstResult(pageNo);
			query.setMaxResults(pageSize);
		}

		List<AttachmentSummaryDTO> list = query.getResultList();

		return list;
	}

	@Override
	public List<AttachmentSummaryDTO> search(AttachmentSummaryDTO dto, UserContext userContext) {
		return search(dto, 0, 0, userContext);
	}

	@Override
	protected List<Predicate> getWhereClause(AttachmentDTO dto, CriteriaBuilder builder, Root<AttachmentDTO> qRoot) {

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

		return whereCondition;

	}

}
