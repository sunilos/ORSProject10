package com.sunilos.common.message;

import javax.validation.constraints.NotEmpty;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;

/**
 * Contains Role form elements and their declarative input validations.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class MessageForm extends BaseForm {

	@NotEmpty
	private String subject = null;

	@NotEmpty
	private String code = null;

	@NotEmpty
	private String type = null;

	@NotEmpty
	private String body = null;

	private String status = null;

	public String html = null ;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public BaseDTO getDto() {
		MessageDTO dto = new MessageDTO();
		dto.setId(id);
		dto.setSubject(subject);
		dto.setCode(code);
		dto.setHtml(html);
		dto.setStatus(status);
		dto.setType(type);
		dto.setBody(body);
		return dto;
	}

	@Override
	public void populate(BaseDTO bdDto) {
	}
}
