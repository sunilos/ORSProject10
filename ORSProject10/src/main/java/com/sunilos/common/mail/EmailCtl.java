package com.sunilos.common.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.ORSResponse;

/**
 * Contains REST API to send an email.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */
@RestController
@RequestMapping(value = "Email")
public class EmailCtl {

	@Autowired
	EmailServiceImpl service = null;

	@GetMapping
	public String display() {
		return "I am fit and fine";
	}

	@GetMapping("Test/{code}")
	public ORSResponse test(@PathVariable String code) {

		System.out.println("Code is recived " + code);

		ORSResponse res = new ORSResponse(true);

		EmailDTO dto = new EmailDTO();
		dto.addTo("sunilsahu007@gmail.com");

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("code", "2222");

		dto.setMessageCode(code, params);

		// dto.addAttachedFilePath("G:/sunRays/books/IMG_0458.JPG");
		// dto.addAttachedFileId(1L);
		// dto.addAttachedFileId(2L);

		service.send(dto, null);
		return res;

	}

	@PostMapping
	public ORSResponse submit(@RequestBody @Valid EmailForm form, BindingResult bindingResult) {

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

		if (res.isSuccess() == false) {
			return res;
		}

		EmailDTO dto = new EmailDTO();
		dto.addTo(form.getTo());
		dto.setSubject(form.getSubject());
		dto.setBody(form.getBody());

		service.send(dto, null);

		return res;

	}

}
