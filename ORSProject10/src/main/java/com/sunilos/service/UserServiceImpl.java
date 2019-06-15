package com.sunilos.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.common.mail.EmailDTO;
import com.sunilos.common.mail.EmailServiceImpl;
import com.sunilos.dao.UserDAOInt;
import com.sunilos.dto.UserDTO;

/**
 * Session facade of Role Service. It is transactional, apply declarative
 * transactions with help of Spring AOP.
 * 
 * If unchecked exception is propagated from a method then transaction is rolled
 * back.
 * 
 * Default propagation value is Propagation.REQUIRED and readOnly = false
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<UserDTO, UserDAOInt> implements UserServiceInt {

	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	EmailServiceImpl emailService = null;

	/**
	 * Find user by login id
	 */
	@Transactional(readOnly = true)
	public UserDTO findByLoginId(String login, UserContext userContext) {
		return baseDao.findByUniqueKey("loginId", login, userContext);
	}

	/**
	 * Authenticate a user
	 */
	@Override
	public UserDTO authenticate(String loginId, String password) {

		UserDTO dto = findByLoginId(loginId, null);
		if (dto != null) {
			UserContext userContext = new UserContext(dto);
			if (password.equals(dto.getPassword())) {
				dto.setLastLogin(new Timestamp((new Date()).getTime()));
				dto.setUnsucessfullLoginAttempt(0);
				update(dto, userContext);
				return dto;
			} else {
				dto.setUnsucessfullLoginAttempt(1 + dto.getUnsucessfullLoginAttempt());
				update(dto, userContext);
			}
		}
		return null;
	}

	/**
	 * Changes password of logged-in user
	 */
	@Override
	public UserDTO changePassword(String loginId, String oldPassword, String newPassword, UserContext userContext) {

		UserDTO dto = findByLoginId(loginId, null);

		if (oldPassword.equals(dto.getPassword())) {

			dto.setPassword(newPassword);
			update(dto, userContext);

			EmailDTO emailDTO = new EmailDTO();
			emailDTO.addTo(dto.getLoginId());

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("user", dto.getFirstName() + " " + dto.getLastName());
			emailDTO.setMessageCode("U-CP", params);

			emailService.send(emailDTO, null);

			return dto;
		} else {
			return null;
		}
	}

	/**
	 * Forgot password
	 */
	@Override
	public UserDTO forgotPassword(String loginId) {

		UserDTO dto = findByLoginId(loginId, null);

		if (dto == null) {
			return null;
		}

		EmailDTO emailDTO = new EmailDTO();
		emailDTO.addTo(dto.getLoginId());

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user", dto.getFirstName() + " " + dto.getLastName());
		params.put("password", dto.getPassword());
		emailDTO.setMessageCode("U-FP", params);

		emailService.send(emailDTO, null);

		return dto;
	}

	/**
	 * Register new user
	 */
	@Override
	public UserDTO register(UserDTO dto) {

		UserContext userContext = new UserContext();
		userContext.setLoginId("super@nenosystems.com");
		userContext.setOrgId(0L);
		userContext.setOrgName("root");

		Long id = add(dto, userContext);

		dto.setId(id);

		EmailDTO emailDTO = new EmailDTO();
		emailDTO.addTo(dto.getLoginId());

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user", dto.getFirstName() + " " + dto.getLastName());
		params.put("login", dto.getLoginId());
		params.put("password", dto.getPassword());
		emailDTO.setMessageCode("U-REG", params);

		emailService.send(emailDTO, userContext);

		return dto;
	}

}
