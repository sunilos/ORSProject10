package com.sunilos.service;

import java.util.List;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.MarksheetDTO;

/**
 * College Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface MarksheetServiceInt extends BaseServiceInt<MarksheetDTO> {

	/**
	 * Finds marksheet by name.
	 * 
	 * @param name
	 * @return
	 */
	public MarksheetDTO findByName(String name, UserContext context);

	/**
	 * Finds marksheet by Roll No
	 * 
	 * @param rollNo
	 * @return
	 */
	public MarksheetDTO findByRollNo(String rollNo, UserContext context);

	/**
	 * Gets merit list of students
	 * 
	 * @return
	 */
	public List<MarksheetDTO> getMeritList(UserContext context);
}