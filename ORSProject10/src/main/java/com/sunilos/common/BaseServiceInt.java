package com.sunilos.common;

import java.util.List;
import java.util.Map;

import com.sunilos.exception.DuplicateRecordException;

/**
 * Role Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface BaseServiceInt<T extends BaseDTO> {

	/**
	 * Adds a Role.
	 * 
	 * @param dto
	 * @return
	 * @throws DuplicateRecordException
	 */
	public long add(T dto, UserContext userContext);

	/**
	 * Updates a Role.
	 * 
	 * @param dto
	 * @throws DuplicateRecordException
	 */
	public void update(T dto, UserContext userContext);

	/*
	 *
	 * Save or update
	 */
	public long save(T dto, UserContext userContext);

	/**
	 * Updates the given fields of a record identified by id. Only the keys
	 * present in the map are applied.
	 *
	 * @param id
	 * @param fields
	 * @param userContext
	 * @return
	 */
	public T updateFields(Long id, Map<String, Object> fields, UserContext userContext);

	/**
	 * Deletes a Role
	 * 
	 * @param id
	 */
	public T delete(long id, UserContext userContext);

	/**
	 * Finds a Role by ID
	 * 
	 * @param id
	 * @return
	 */
	public T findById(long id, UserContext userContext);

	/**
	 * Searches Roles with pagination.
	 * 
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<T> search(T dto, int pageNo, int pageSize, UserContext userContext);

	/**
	 * Searches Roles
	 * 
	 * @param dto
	 * @return
	 */
	public List search(T dto, UserContext userContext);

}