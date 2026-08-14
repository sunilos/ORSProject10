package com.sunilos.ctl;

import java.util.Enumeration;
import java.util.LinkedHashSet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.MenuItem;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.UserDTO;
import com.sunilos.form.LoginForm;
import com.sunilos.form.UserForm;
import com.sunilos.form.UserRegistrationForm;
import com.sunilos.service.UserServiceInt;
import com.sunilos.util.JwtUtil;

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
@RequestMapping(value = "auth")
public class LoginCtl extends BaseCtl<UserForm, UserDTO, UserServiceInt> {

	/**
	 * Finds user by login id
	 * 
	 * @param loginId
	 * @return
	 */
	@GetMapping("login/{loginId}")
	public ResponseEntity<ORSResponse> get(@PathVariable String loginId) {
		ORSResponse res = new ORSResponse(true);
		UserDTO dto = baseService.findByLoginId(loginId, userContext);
		System.out.println("User " + dto);
		if (dto == null) {
			return errorResponse(res, "Record not found", HttpStatus.NOT_FOUND);
		}
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(dto.getFirstName());
		userDTO.setLastName(dto.getLastName());
		userDTO.setLoginId(dto.getLoginId());
		res.addData(userDTO);
		return okResponse(res);
	}

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("login")
	public ResponseEntity<ORSResponse> login(@RequestBody @Valid LoginForm form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (!res.isSuccess()) {
			return errorResponse(res, HttpStatus.BAD_REQUEST);
		}

		UserDTO dto = baseService.authenticate(form.getLoginId(), form.getPassword());
		if (dto == null) {
			return errorResponse(res, "Invalid ID or Password", HttpStatus.UNAUTHORIZED);
		}

		String token = jwtUtil.generateToken(dto.getLoginId());
		res.addData(dto);
		res.addResult("token", token);
		return okResponse(res);

	}

	/**
	 * Emails password to user
	 * 
	 * @return
	 */
	@GetMapping("fp/{login}")
	public ResponseEntity<ORSResponse> forgotPassword(@PathVariable String login, HttpServletRequest request) {

		Enumeration<String> e = request.getHeaderNames();
		String key = null;
		while (e.hasMoreElements()) {
			key = e.nextElement();
			System.out.println(key + " = " + request.getHeader(key));
		}

		ORSResponse res = new ORSResponse(true);
		UserDTO dto = this.baseService.forgotPassword(login);
		if (dto == null) {
			return errorResponse(res, "Invalid Login Id", HttpStatus.NOT_FOUND);
		}
		res.addMessage("Password has been sent to email id");
		return okResponse(res);
	}

	/**
	 * Register new user
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("signUp")
	public ResponseEntity<ORSResponse> signUp(@RequestBody @Valid UserRegistrationForm form,
			BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (!res.isSuccess()) {
			return errorResponse(res, HttpStatus.BAD_REQUEST);
		}

		UserDTO dto = baseService.findByLoginId(form.getLogin(), userContext);

		if (dto != null) {
			return errorResponse(res, "Login Id already exists", HttpStatus.CONFLICT);
		}

		dto = new UserDTO();
		dto.setFirstName(form.getFirstName());
		dto.setLastName(form.getLastName());
		dto.setLoginId(form.getLogin());
		dto.setGender(form.getGender());
		dto.setDob(form.getDob());
		dto.setPhone(form.getMobileNo());

		baseService.register(dto);

		res.addMessage("User has been registered");
		return okResponse(res);
	}

	@GetMapping("menu")
	public ResponseEntity<ORSResponse> menu(HttpSession session) {

		LinkedHashSet<MenuItem> menuBar = new LinkedHashSet<MenuItem>();

		MenuItem std = new MenuItem("Student", "/student");
		std.addSubmenu("New Student", "/student");
		std.addSubmenu("Student List", "/studentlist");

		menuBar.add(std);

		MenuItem coll = new MenuItem("College", "/college");
		coll.addSubmenu("New College", "/college");
		coll.addSubmenu("College List", "/collegelist");

		menuBar.add(coll);

		ORSResponse res = new ORSResponse(true);
		res.addData(menuBar);
		return okResponse(res);
	}

}
