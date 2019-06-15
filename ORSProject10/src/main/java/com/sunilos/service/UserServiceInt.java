package com.sunilos.service;

import com.sunilos.common.BaseServiceInt;
import com.sunilos.common.UserContext;
import com.sunilos.dto.UserDTO;

/**
 * College Service interface.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface UserServiceInt extends BaseServiceInt<UserDTO> {

	/**
	 * Finds User by name.
	 * 
	 * @param name
	 * @return
	 */
	public UserDTO findByLoginId(String name, UserContext userContext);

	public UserDTO authenticate(String loginId, String password);

	public UserDTO changePassword(String loginId, String oldPassword, String newPassword, UserContext userContext);

	public UserDTO forgotPassword(String loginId);

	public UserDTO register(UserDTO dto);

}
