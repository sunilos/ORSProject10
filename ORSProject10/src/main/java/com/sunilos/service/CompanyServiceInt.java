package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.CompanyDTO;

/**
 * Company Service interface.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface CompanyServiceInt extends BaseServiceInt<CompanyDTO> {

	/**
	 * Finds a Company by name.
	 *
	 * @param name
	 * @return
	 */
	public CompanyDTO findByName(String name, UserContext context);

}
