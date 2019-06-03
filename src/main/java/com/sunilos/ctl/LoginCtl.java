package com.sunilos.ctl;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.common.UserContext;
import com.sunilos.dto.UserDTO;
import com.sunilos.form.LoginForm;
import com.sunilos.form.UserForm;
import com.sunilos.form.UserRegistrationForm;
import com.sunilos.service.UserServiceInt;

/**
 * Login controller provides API for Sign Up, Sign In and Forgot password
 * operations
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */
@RestController
@RequestMapping(value = "Auth")
public class LoginCtl extends BaseCtl<UserForm, UserDTO, UserServiceInt> {

	/**
	 * Finds user by login id
	 * 
	 * @param loginId
	 * @return
	 */
	@GetMapping("login/{loginId}")
	public ORSResponse get(@PathVariable String loginId) {
		ORSResponse res = new ORSResponse(true);
		UserDTO dto = baseService.findByLoginId(loginId, userContext);
		System.out.println("User " + dto);
		if (dto != null) {
			UserDTO userDTO = new UserDTO();
			userDTO.setFirstName(dto.getFirstName());
			userDTO.setLastName(dto.getLastName());
			userDTO.setLoginId(dto.getLoginId());
			res.addData(userDTO);
		} else {
			res.setSuccess(false);
			res.addMessage("Record not found");
		}
		return res;
	}

	@PostMapping("login")
	public ORSResponse login(@RequestBody @Valid LoginForm form, BindingResult bindingResult, HttpSession session) {

		ORSResponse res = valiate(bindingResult);

		if (!res.isSuccess()) {
			return res;
		}

		UserDTO dto = baseService.authenticate(form.getLoginId(), form.getPassword());
		if (dto == null) {
			res.setSuccess(false);
			res.addMessage("Invalid ID or Password");
		} else {
			UserContext context = new UserContext(dto);
			session.setAttribute("userContext", context);
			res.setSuccess(true);
			res.addMessage("Login is successful");
		}
		return res;

	}

	/**
	 * Emails password to user
	 * 
	 * @return
	 */
	@GetMapping("fp/{login}")
	public ORSResponse forgotPassword(@PathVariable String login) {
		ORSResponse res = new ORSResponse(true);
		UserDTO dto = this.baseService.forgotPassword(login);
		if (dto == null) {
			res.setSuccess(false);
			res.addMessage("Invalid Login Id");
		} else {
			res.setSuccess(true);
			res.addMessage("Password has been sent to email id");
		}
		return res;
	}

	/**
	 * Register new user
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("signUp")
	public ORSResponse signUp(@RequestBody @Valid UserRegistrationForm form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (!res.isSuccess()) {
			return res;
		}

		UserDTO dto = baseService.authenticate(form.getLogin(), form.getPassword());

		if (dto != null) {
			res.setSuccess(false);
			res.addMessage("Login Id already exists");
			return res;
		}

		dto = new UserDTO();
		dto.setFirstName(form.getFirstName());
		dto.setLastName(form.getLastName());
		dto.setLoginId(form.getLogin());
		dto.setGender(form.getGender());
		dto.setDob(form.getDob());

		baseService.register(dto, userContext);

		res.setSuccess(true);
		res.addMessage("User has been registered");
		return res;
	}

}
