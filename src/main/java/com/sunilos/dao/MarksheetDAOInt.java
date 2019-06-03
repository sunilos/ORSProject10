package com.sunilos.dao;

import java.util.List;

import com.sunilos.common.BaseDAOInt;
import com.sunilos.dto.MarksheetDTO;

/**
 * Marksheet DAO interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public interface MarksheetDAOInt extends BaseDAOInt<MarksheetDTO> {

	/**
	 * Get merit list
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<MarksheetDTO> getMeritList();
}
