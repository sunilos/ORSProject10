package com.sunilos.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sunilos.dto.UserDTO;
import com.sunilos.exception.DatabaseException;
import com.sunilos.exception.DuplicateRecordException;
import com.sunilos.service.UserServiceInt;

/**
 * Base controller class contains get, search, save, delete REST APIs
 *
 * <p>
 * Endpoints exposed by every subclass (relative to its own
 * {@code @RequestMapping}):
 *
 * <ul>
 * <li>GET /info - health check</li>
 * <li>GET / - list all entities</li>
 * <li>GET /{id} - get entity by ID</li>
 * <li>GET|POST /search - search entities by form attributes, paginated</li>
 * <li>GET|POST /search/{pageNo} - search entities by form attributes, page
 * size 5</li>
 * <li>POST / - add entity, or update it when the form carries an existing
 * ID</li>
 * <li>PUT /{id} - replace entity identified by ID</li>
 * <li>PATCH /{id} - partially update entity fields identified by ID</li>
 * <li>DELETE /{id} - delete entity by ID</li>
 * </ul>
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 * @param <F>
 * @param <T>
 * @param <S>
 */
public abstract class BaseCtl<F extends BaseForm, T extends BaseDTO, S extends BaseServiceInt<T>> {

	/**
	 * Form operations, used to calculate the next page number in
	 * {@link #search(BaseForm)}
	 */
	protected static final String OP_NEXT = "Next";
	protected static final String OP_PREVIOUS = "Previous";

	@Autowired
	protected S baseService = null;

	@Autowired
	private UserServiceInt userService;

	@Value("${page.size}")
	private int pageSize = 0;

	@Autowired
	private Validator validator;

	/**
	 * Contains context of logged-in user
	 */
	protected UserContext userContext = null;

	/**
	 * Resolves the UserContext for the current request. For protected
	 * endpoints, {@link FrontCtl} has already validated the JWT Bearer token
	 * and stored the resolved loginId in request attribute "loginId" before
	 * this runs. Public endpoints (e.g. /auth/login) never reach the
	 * interceptor, so they fall through to a default, org-less context.
	 *
	 * @param request
	 */
	@ModelAttribute
	public void setUserContext(HttpServletRequest request) {

		String loginId = (String) request.getAttribute("loginId");

		if (loginId != null) {
			UserDTO dto = userService.findByLoginId(loginId, null);
			if (dto != null) {
				userContext = new UserContext(dto);
				return;
			}
		}

		UserDTO dto = new UserDTO();
		dto.setLoginId("root@sunilos.com");
		dto.setFirstName("Dummy name");
		dto.setLastName("Dummy name");
		dto.setOrgId(0L);
		dto.setRoleId(1L);
		dto.setOrgName("root");
		userContext = new UserContext(dto);
	}

	/**
	 * Wraps a successful ORSResponse with HTTP 200.
	 *
	 * @param res
	 * @return
	 */
	protected ResponseEntity<ORSResponse> okResponse(ORSResponse res) {
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	/**
	 * Marks the response as failed, attaches the given message, and wraps it
	 * with the given HTTP status code.
	 *
	 * @param res
	 * @param message
	 * @param status
	 * @return
	 */
	protected ResponseEntity<ORSResponse> errorResponse(ORSResponse res, String message, HttpStatus status) {
		res.setSuccess(false);
		res.addMessage(message);
		return ResponseEntity.status(status).body(res);
	}

	/**
	 * Marks an already-populated failure response (e.g. from {@link #valiate})
	 * as failed and wraps it with the given HTTP status code.
	 *
	 * @param res
	 * @param status
	 * @return
	 */
	protected ResponseEntity<ORSResponse> errorResponse(ORSResponse res, HttpStatus status) {
		res.setSuccess(false);
		return ResponseEntity.status(status).body(res);
	}

	/**
	 * Default get mapping
	 *
	 * @return
	 */
	@GetMapping("info")
	public ResponseEntity<ORSResponse> info() {
		ORSResponse res = new ORSResponse(true);
		res.addData("I am okay " + this.getClass() + " --" + new Date());
		return okResponse(res);
	}

	/**
	 * Get all entities
	 *
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ORSResponse> get() {
		ORSResponse res = new ORSResponse(true);
		res.addData(baseService.search(newDto(), userContext));
		return okResponse(res);
	}

	/**
	 * Get entity by primary key ID
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("{id}")
	public ResponseEntity<ORSResponse> get(@PathVariable long id) {
		ORSResponse res = new ORSResponse(true);
		T dto = baseService.findById(id, userContext);
		if (dto != null) {
			res.addData(dto);
			return okResponse(res);
		}
		return errorResponse(res, "Record not found", HttpStatus.NOT_FOUND);
	}

	/**
	 * Delete entity by primary key ID
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("{id}")
	public ResponseEntity<ORSResponse> delete(@PathVariable long id) {
		ORSResponse res = new ORSResponse(true);
		try {
			T dto = baseService.delete(id, userContext);
			res.addData(dto);
			return okResponse(res);
		} catch (DatabaseException e) {
			return errorResponse(res, e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return errorResponse(res, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Creates an empty instance of the DTO handled by this controller, used as
	 * an unfiltered criteria object when listing all entities.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T newDto() {
		Class<?>[] typeArgs = GenericTypeResolver.resolveTypeArguments(getClass(), BaseCtl.class);
		try {
			return (T) typeArgs[1].getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Unable to instantiate DTO for " + getClass(), e);
		}
	}

	/**
	 * Creates an empty instance of the form handled by this controller, used to
	 * validate the fields supplied to {@link #patch}.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private F newForm() {
		Class<?>[] typeArgs = GenericTypeResolver.resolveTypeArguments(getClass(), BaseCtl.class);
		try {
			return (F) typeArgs[0].getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Unable to instantiate form for " + getClass(), e);
		}
	}

	/**
	 * Search entities by form attributes
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ORSResponse> search(@RequestBody F form) {

		// Calculate next page number
		String operation = form.getOperation();
		int pageNo = form.getPageNo();

		if (OP_NEXT.equals(operation)) {
			pageNo++;
		} else if (OP_PREVIOUS.equals(operation)) {
			pageNo--;
		}

		// 0 is first page index
		pageNo = (pageNo < 0) ? 0 : pageNo;

		form.setPageNo(pageNo);

		T dto = (T) form.getDto();

		ORSResponse res = new ORSResponse(true);

		System.out.println("Page No is****************" + pageNo);
		System.out.println("Page size is****************" + pageSize);

		List<T> list = baseService.search(dto, pageNo, pageSize, userContext);

		System.out.println("Records is****************" + list.size());

		res.addData(list);

		return okResponse(res);
	}

	@RequestMapping(value = "/search/{pageNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ORSResponse> search(@RequestBody F form, @PathVariable int pageNo) {

		// 0 is first page index
		pageNo = (pageNo < 0) ? 0 : pageNo;

		T dto = (T) form.getDto();

		ORSResponse res = new ORSResponse(true);

		System.out.println(pageNo + "Page size is****************" + pageSize);

		res.addData(baseService.search(dto, pageNo, 5, userContext));

		return okResponse(res);
	}

	@PostMapping()
	public ResponseEntity<ORSResponse> add(@RequestBody @Valid F form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (res.isSuccess() == false) {
			return errorResponse(res, HttpStatus.BAD_REQUEST);
		}

		try {
			T dto = (T) form.getDto();
			System.out.println("---------------------------------------------->" + dto);
			if (dto.getId() != null && dto.getId() > 0) {
				baseService.update(dto, userContext);
			} else {
				baseService.add(dto, userContext);
			}
			res.addData(dto.getId());
			return okResponse(res);
		} catch (DuplicateRecordException e) {
			return errorResponse(res, e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			e.printStackTrace();
			return errorResponse(res, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Replace entity identified by ID with the given form data
	 *
	 * @param id
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PutMapping("{id}")
	public ResponseEntity<ORSResponse> update(@PathVariable long id, @RequestBody @Valid F form,
			BindingResult bindingResult) {
		System.out.println("I am in put method of base ctl");

		ORSResponse res = valiate(bindingResult);

		if (res.isSuccess() == false) {
			return errorResponse(res, HttpStatus.BAD_REQUEST);
		}

		try {
			form.setId(id);
			T dto = (T) form.getDto();
			baseService.update(dto, userContext);
			res.addData(dto.getId());
			return okResponse(res);
		} catch (DuplicateRecordException e) {
			return errorResponse(res, e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return errorResponse(res, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Extracts field-level validation errors from the binding result.
	 *
	 * @param br
	 * @return
	 */
	protected Map<String, String> fieldErrors(BindingResult br) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError e : br.getFieldErrors()) {
			errors.put(e.getField(), e.getDefaultMessage());
		}
		return errors;
	}

	/**
	 * Partially update entity identified by ID. Only the keys present in the
	 * request body are applied.
	 *
	 * @param id
	 * @param fields
	 * @return
	 */
	@PatchMapping("{id}")
	public ResponseEntity<ORSResponse> patch(@PathVariable long id, @RequestBody Map<String, Object> fields) {
		ORSResponse res = new ORSResponse(true);

		// Validate the supplied fields using a MarksheetForm and the same validator
		// used for POST/PUT
		// Create form object
		F form = newForm();
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(form);
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			if (wrapper.isWritableProperty(entry.getKey())) {
				wrapper.setPropertyValue(entry.getKey(), entry.getValue());
			}
		}

		// validate the form
		DataBinder binder = new DataBinder(form, "form");
		binder.setValidator(validator);
		binder.validate();
		BindingResult bindingResult = binder.getBindingResult();

		// Check if record exists
		if (bindingResult.hasErrors()) {

			ORSResponse errors = valiate(bindingResult);
			Map<String, String> errorsMap = errors.getInputErrors();
			errorsMap.keySet().retainAll(fields.keySet()); // only supplied fields can fail
			if (!errorsMap.isEmpty()) {
				return errorResponse(errors, "Validation failed", HttpStatus.BAD_REQUEST);
			}
		}

		try {
			T dto = baseService.updateFields(id, fields, userContext);
			res.addData(dto);
			return okResponse(res);
		} catch (DatabaseException e) {
			return errorResponse(res, e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return errorResponse(res, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Gets input error messages and put into REST response
	 * 
	 * @param bindingResult
	 * @return
	 */
	public ORSResponse valiate(BindingResult bindingResult) {
		ORSResponse res = new ORSResponse(true);

		if (bindingResult.hasErrors()) {

			res.setSuccess(false);

			Map<String, String> errors = new HashMap<String, String>();

			List<FieldError> list = bindingResult.getFieldErrors();
			// Lambda expression Java 8 feature
			list.forEach(e -> {
				errors.put(e.getField(), e.getDefaultMessage());
			});
			res.addInputErrors(errors);
		}
		return res;

	}

}
