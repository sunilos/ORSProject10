package com.sunilos.common;

import java.util.List;

/**
 * 
 * Role DAO interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface BaseDAOInt<T extends BaseDTO> {

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
	 * Deletes a Role.
	 * 
	 * @param dto
	 */
	public void delete(T dto, UserContext userContext);

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
	 * Searches Role with pagination.
	 * 
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List search(T dto, int pageNo, int pageSize, UserContext userContext);

	/**
	 * Seraches Role.
	 * 
	 * @param dto
	 * @return
	 */
	public List search(T dto, UserContext userContext);

	public List runHQL(String hql, UserContext userContext);

}
