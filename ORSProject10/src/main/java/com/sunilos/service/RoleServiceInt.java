package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.RoleDTO;

/**
 * College Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface RoleServiceInt extends BaseServiceInt<RoleDTO> {

	/**
	 * Finds Role by name.
	 * 
	 * @param name
	 * @return
	 */
	public RoleDTO findByName(String name, UserContext userContext);

}
