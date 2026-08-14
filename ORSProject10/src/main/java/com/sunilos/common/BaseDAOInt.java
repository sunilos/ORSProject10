package com.sunilos.common;

import java.util.List;
import java.util.Map;

/**
 *
 * Role DAO interface.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface BaseDAOInt<T extends BaseDTO> {

	public abstract Class<T> getDTOClass();

	public String getDTOClassName();

	/**
	 * Checks whether a record exists by Primary Key.
	 *
	 * @param id
	 * @return
	 */
	public boolean exists(long id);

	/**
	 * Finds Role by Primary Key.
	 *
	 * @param pk
	 * @return
	 */
	public T findByPK(long pk, UserContext userContext);

	/**
	 * Find record by unique key
	 *
	 * @param attribute
	 * @param val
	 * @param dtoClass
	 * @return
	 */
	public T findByUniqueKey(String attribute, Object val, UserContext userContext);

	/**
	 * Finds all Role with pagination.
	 *
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<T> findAll(T dto, int pageNo, int pageSize, UserContext userContext);

	/**
	 * Finds all Role.
	 *
	 * @param dto
	 * @return
	 */
	public List<T> findAll(T dto, UserContext userContext);

	public List runHQL(String hql, UserContext userContext);

	/**
	 * Adds a Role.
	 *
	 * @param dto
	 * @return
	 */
	public long add(T dto, UserContext userContext);

	/**
	 * Updates a Role.
	 *
	 * @param dto
	 */
	public void update(T dto, UserContext userContext);

	/**
	 * Updates the given fields of a record identified by id. Only the keys
	 * present in the map are applied; id and audit columns are protected from
	 * being overwritten.
	 *
	 * @param id
	 * @param fields
	 * @param userContext
	 * @return
	 */
	public T updateFields(Long id, Map<String, Object> fields, UserContext userContext);

	/**
	 * Deletes a Role.
	 *
	 * @param dto
	 */
	public void delete(T dto, UserContext userContext);

	public void populate(T dto, UserContext userContext);

}
