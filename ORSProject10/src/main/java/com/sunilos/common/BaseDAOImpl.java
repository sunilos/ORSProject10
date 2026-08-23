package com.sunilos.common;

import static com.sunilos.util.DataValidator.isZeroNumber;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.exception.DatabaseException;
import com.sunilos.exception.DuplicateRecordException;

public abstract class BaseDAOImpl<T extends BaseDTO> implements BaseDAOInt<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		// this.sessionFactory =
		// entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
	}

	@Override
	public String getDTOClassName() {
		return getDTOClass().getSimpleName();
	}

	@Override
	public boolean exists(long id) {
		Long count = entityManager.createQuery(
				"select count(m) from " + getDTOClass().getSimpleName() + " m where m.id = :id", Long.class)
				.setParameter("id", id)
				.getSingleResult();
		return count > 0;
	}

	public T findByPK(long pk, UserContext userContext) {
		T dto = entityManager.find(getDTOClass(), pk);
		return dto;
	}

	/**
	 * Find record by Unique key
	 *
	 * @param attribute
	 * @param val
	 * @param dtoClass
	 * @return
	 */
	public T findByUniqueKey(String attribute, Object val, UserContext userContext) {

		Class<T> dtoClass = getDTOClass();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> cq = builder.createQuery(dtoClass);

		Root<T> qRoot = cq.from(dtoClass);

		Predicate condition = builder.equal(qRoot.get(attribute), val);

		if (userContext != null && !isZeroNumber(userContext.getOrgId())) {
			Predicate conditionGrp = builder.equal(qRoot.get("orgId"), userContext.getOrgId());
			cq.where(condition, conditionGrp);
		} else {
			cq.where(condition);
		}

		TypedQuery<T> query = entityManager.createQuery(cq);

		List<T> list = query.getResultList();

		T dto = null;

		if (list.size() > 0) {
			dto = list.get(0);
		}

		return dto;

	}

	public List<T> findAll(T dto, int pageNo, int pageSize, UserContext userContext) {

		TypedQuery<T> query = createCriteria(dto, userContext);

		if (pageSize > 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}

		System.out.println("Page No is****************" + pageNo
				+ "Page size is****************" + pageSize);
		return query.getResultList();
	}

	public List<T> findAll(T dto, UserContext userContext) {
		return findAll(dto, 0, 0, userContext);
	}

	/**
	 * Build criteria query
	 *
	 * @param dto
	 * @return
	 */
	protected TypedQuery<T> createCriteria(T dto, UserContext userContext) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Create criteria
		Class<T> dtoClass = getDTOClass();
		CriteriaQuery<T> cq = builder.createQuery(dtoClass);

		// Columns information
		Root<T> qRoot = cq.from(dtoClass);

		// Column of query
		cq.select(qRoot);

		// Create where conditions
		List<Predicate> whereClause = getWhereClause(dto, builder, qRoot);

		// Put organization filter
		if (dto.isGroupFilter()) {
			whereClause.add(builder.equal(qRoot.get("orgId"), dto.getOrgId()));
		}

		if (!whereClause.isEmpty()) {
			cq.where(whereClause.toArray(new Predicate[0]));
		}

		List<Order> orderBy = getOrderByClause(dto, builder, qRoot);

		if (!orderBy.isEmpty()) {
			cq.orderBy(orderBy.toArray(new Order[0]));
		}

		return entityManager.createQuery(cq);

	}

	/**
	 * Creates WHERE clause of search
	 *
	 * @param dto
	 * @param builder
	 * @param qRoot
	 * @return
	 */
	protected abstract List<Predicate> getWhereClause(T dto, CriteriaBuilder builder, Root<T> qRoot);

	/**
	 * Get order by clause
	 *
	 * @param dto
	 * @param builder
	 * @param qRoot
	 * @return
	 */
	protected List<Order> getOrderByClause(T dto, CriteriaBuilder builder, Root<T> qRoot) {

		// Apply Order by clause

		LinkedHashMap<String, String> map = dto.orderBY();

		List<Order> orderBy = new ArrayList<Order>();

		map.forEach((key, value) -> {
			if (value.equals("asc")) {
				orderBy.add(builder.asc(qRoot.get(key)));
			} else {
				orderBy.add(builder.desc(qRoot.get(key)));
			}
		});

		return orderBy;
	}

	/**
	 * Run HQL query
	 *
	 * @param hql
	 * @param userContext
	 * @return
	 */
	public List runHQL(String hql, UserContext userContext) {
		Query q = entityManager.createQuery(hql);
		List l = q.getResultList();
		return l;
	}

	/**
	 * Add a record
	 */
	public long add(T dto, UserContext userContext) {

		checkDuplicate(dto, userContext);

		dto.setCreatedBy(userContext.getLoginId());
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedBy(userContext.getLoginId());
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		dto.setOrgId(userContext.getOrgId());
		dto.setOrgName(userContext.getOrgName());

		populate(dto, userContext);

		entityManager.persist(dto);

		return dto.getId();

	}

	/**
	 * Update a record
	 */
	public void update(T dto, UserContext userContext) {
		checkDuplicate(dto, userContext);

		dto.setModifiedBy(userContext.getLoginId());
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

		populate(dto, userContext);

		entityManager.merge(dto);
	}

	/**
	 * Populate redundant values into dto. Overridden by chiled classes.
	 *
	 * @param dto
	 */
	public void populate(T dto, UserContext userContext) {

	}

	/**
	 * Check unique keys
	 *
	 * @param dto
	 */
	private void checkDuplicate(T dto, UserContext userContext) {
		LinkedHashMap<String, Object> uniqueKeys = dto.uniqueKeys();
		if (uniqueKeys == null) {
			return;
		}
		uniqueKeys.forEach((key, value) -> {
			T dtoExist = findByUniqueKey(key, value, userContext);
			if (dtoExist != null && dto.getId() != dtoExist.getId()) {
				throw new DuplicateRecordException(key + " already exists");
			}
		});
	}

	@Override
	@Transactional
	public T updateFields(Long id, Map<String, Object> fields, UserContext userContext) {

		if (!exists(id)) {
			throw new DatabaseException(getDTOClass().getSimpleName() + " not found for id: " + id);
		}

		fields.remove("id");
		fields.remove("createdBy");
		fields.remove("createdBy");
		fields.remove("modifiedDatetime");
		fields.remove("createdDatetime");

		if (!fields.isEmpty()) {
			StringBuilder hql = new StringBuilder("update " + getDTOClass().getSimpleName() + " set ");

			int i = 0;
			for (String key : fields.keySet()) {
				hql.append(key).append(" = :").append(key);
				if (++i < fields.size()) {
					hql.append(", ");
				}
			}
			hql.append(" where id = :id");

			Query query = entityManager.createQuery(hql.toString());
			fields.forEach(query::setParameter);
			query.setParameter("id", id);

			int updated = query.executeUpdate();
			if (updated == 0) {
				throw new DatabaseException(getDTOClass().getSimpleName() + " not found for id: " + id);
			}

			entityManager.clear();
		}

		return findByPK(id, userContext);
	}

	/**
	 * Delete a record
	 */
	public void delete(T dto, UserContext userContext) {
		if (dto != null) {
			entityManager.remove(dto);
		}
	}

}
