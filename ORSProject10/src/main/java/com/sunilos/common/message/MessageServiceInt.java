package com.sunilos.common.message;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;

/**
 * College Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface MessageServiceInt extends BaseServiceInt<MessageDTO> {

	/**
	 * Finds Role by name.
	 * 
	 * @param name
	 * @return
	 */
	public MessageDTO findByTitle(String name, UserContext userContext);

	public MessageDTO findByCode(String code, UserContext userContext);

}
