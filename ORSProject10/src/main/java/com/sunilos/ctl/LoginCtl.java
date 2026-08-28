package com.sunilos.ctl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Collections;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.sunilos.dto.RoleDTO;
import com.sunilos.dto.UserDTO;
import com.sunilos.exception.DuplicateRecordException;
import com.sunilos.form.LoginForm;
import com.sunilos.form.UserForm;
import com.sunilos.form.UserRegistrationForm;
import com.sunilos.service.RoleServiceInt;
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

	private static final Logger log = LoggerFactory.getLogger(LoginCtl.class);

	/**
	 * Role assigned to users who self-register via {@link #signUp}.
	 */
	private static final String SELF_SIGNUP_ROLE = "Guest";

	@Autowired
	private RoleServiceInt roleService;

	/**
	 * Minimum time between two forgot-password requests for the same login
	 * id, to slow down mail-flooding / brute-force enumeration of this
	 * unauthenticated endpoint.
	 */
	private static final long FORGOT_PASSWORD_COOLDOWN_MS = 60_000L;

	/**
	 * Caps memory use of {@link #forgotPasswordNextAllowedAt} by evicting the
	 * least-recently-used login id once the tracked set grows past this size.
	 */
	private static final int FORGOT_PASSWORD_MAX_TRACKED_LOGINS = 10_000;

	private final Map<String, Long> forgotPasswordNextAllowedAt = Collections
			.synchronizedMap(new LinkedHashMap<String, Long>(16, 0.75f, true) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
					return size() > FORGOT_PASSWORD_MAX_TRACKED_LOGINS;
				}
			});

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
	 * Emails password to user. Always reports the same generic outcome
	 * regardless of whether the login id exists, so this unauthenticated
	 * endpoint can't be used to enumerate registered accounts.
	 *
	 * @return
	 */
	@PostMapping("fp/{login}")
	public ResponseEntity<ORSResponse> forgotPassword(@PathVariable String login) {

		ORSResponse res = new ORSResponse(true);
		res.addMessage("If that login id exists, password reset instructions have been sent to its email");

		if (!tryAcquireForgotPasswordSlot(login)) {
			return okResponse(res);
		}

		this.baseService.forgotPassword(login);
		return okResponse(res);
	}

	/**
	 * Rate-limits forgot-password requests per login id. Returns {@code true}
	 * (and starts a fresh cooldown) if a request for this login id is
	 * currently allowed, {@code false} if one was already made within
	 * {@link #FORGOT_PASSWORD_COOLDOWN_MS}.
	 */
	private boolean tryAcquireForgotPasswordSlot(String login) {
		String key = login == null ? "" : login.trim().toLowerCase();
		long now = System.currentTimeMillis();
		synchronized (forgotPasswordNextAllowedAt) {
			Long nextAllowedAt = forgotPasswordNextAllowedAt.get(key);
			if (nextAllowedAt != null && nextAllowedAt > now) {
				return false;
			}
			forgotPasswordNextAllowedAt.put(key, now + FORGOT_PASSWORD_COOLDOWN_MS);
			return true;
		}
	}

	/**
	 * Register new user
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("register")
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
		dto.setEmail(form.getLogin());
		dto.setPassword(form.getPassword());
		dto.setGender(form.getGender());
		dto.setDob(form.getDob());
		dto.setPhone(form.getMobileNo());

		RoleDTO role = roleService.findByName(SELF_SIGNUP_ROLE, userContext);
		if (role != null) {
			dto.setRoleId(role.getId());
			dto.setRoleName(role.getName());
		} else {
			log.warn("'{}' role not found; registering user {} without a role", SELF_SIGNUP_ROLE, form.getLogin());
		}

		try {
			baseService.register(dto);
		} catch (DuplicateRecordException e) {
			return errorResponse(res, "Login Id already exists", HttpStatus.CONFLICT);
		}

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
