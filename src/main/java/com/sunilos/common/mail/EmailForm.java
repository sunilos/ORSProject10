package com.sunilos.common.mail;

import javax.validation.constraints.NotEmpty;

/**
 * Contains Email data received from UI
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class EmailForm {

	public static String HTML = "H";
	public static String PLAIN = "P";

	@NotEmpty
	private String to = null;

	@NotEmpty
	private String subject = null;

	@NotEmpty
	private String body = null;

	/**
	 * Type of email
	 */
	private String type = PLAIN;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
