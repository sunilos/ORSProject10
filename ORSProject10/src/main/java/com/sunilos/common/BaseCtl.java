package com.sunilos.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sunilos.dto.UserDTO;

/**
 * Base controller class contains get, search, save, delete REST APIs
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
	 * Form operations
	 */
	protected static final String OP_SAVE = "Save";
	protected static final String OP_NEW = "New";
	protected static final String OP_DELETE = "Delete";
	protected static final String OP_CANCEL = "Cancel";
	protected static final String OP_ERROR = "Error";
	protected static final String OP_NEXT = "Next";
	protected static final String OP_PREVIOUS = "Previous";
	protected static final String OP_LOGOUT = "Logout";
	protected static final String OP_GO = "Go";
	protected static final String OP_GET = "Get";

	@Autowired
	protected S baseService = null;

	@Value("${page.size}")
	private int pageSize = 0;

	/**
	 * Contains context of logged-in user
	 */
	protected UserContext userContext = null;

	/**
	 * Get user context from session
	 * 
	 * @param session
	 */
	@ModelAttribute
	public void setUserContext(HttpSession session) {
		userContext = (UserContext) session.getAttribute("userContext");
		if(userContext == null){
			UserDTO dto  = new UserDTO();
			dto.setLoginId("root@sunilos.com");
			dto.setFirstName("Dummy name");
			dto.setLastName("Dummy name");
			dto.setOrgId(0L);
			dto.setRoleId(1L);
			dto.setOrgName("root");
			userContext = new UserContext(dto);
		}
	
	}

	/**
	 * Default get mapping
	 * 
	 * @return
	 */
	@GetMapping
	public ORSResponse get() {
		ORSResponse res = new ORSResponse(true);
		res.addData("I am okay " + this.getClass() + " --" + new Date());
		return res;
	}

	/**
	 * Get entity by primary key ID
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("get/{id}")
	public ORSResponse get(@PathVariable long id) {
		ORSResponse res = new ORSResponse(true);
		T dto = baseService.findById(id, userContext);
		if (dto != null) {
			res.addData(dto);
		} else {
			res.setSuccess(false);
			res.addMessage("Record not found");
		}
		return res;
	}

	/**
	 * Delete entity by primary key ID
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("delete/{id}")
	public ORSResponse delete(@PathVariable long id) {
		ORSResponse res = new ORSResponse(true);
		try {
			T dto = baseService.delete(id, userContext);
			res.addData(dto);
		} catch (Exception e) {
			res.setSuccess(false);
			res.addMessage(e.getMessage());
		}
		return res;
	}

	/**
	 * Search entities by form attributes
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	public ORSResponse search(@RequestBody F form) {

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

		System.out.println("Page size is****************" + pageSize);

		res.addData(baseService.search(dto, pageNo, pageSize, userContext));

		return res;
	}

	@RequestMapping(value = "/search/{pageNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public ORSResponse search(@RequestBody F form, @PathVariable int pageNo) {

		// 0 is first page index
		pageNo = (pageNo < 0) ? 0 : pageNo;

		T dto = (T) form.getDto();

		ORSResponse res = new ORSResponse(true);

		System.out.println(pageNo + "Page size is****************" + pageSize);

		res.addData(baseService.search(dto, pageNo, 5, userContext));

		return res;
	}

	@PostMapping("/save")
	public ORSResponse save(@RequestBody @Valid F form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (res.isSuccess() == false) {
			return res;
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
		} catch (Exception e) {
			res.setSuccess(false);
			res.addMessage(e.getMessage());
			e.printStackTrace();
		}
		return res;
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
