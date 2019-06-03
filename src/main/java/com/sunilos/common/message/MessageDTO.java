package com.sunilos.common.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Email/SMS Message class. Message subject and body contains place holders.
 * Place holders defined inside subject/body by defined by {0}, {1}, {3},.. etc.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_MESSAGE")
public class MessageDTO extends BaseDTO {

	public static String ACTIVE = "Active";
	public static String INACTIVE = "Inactive";

	public static String EMAIL = "Email";
	public static String SMS = "SMS";

	@Column(name = "CODE", length = 10, unique = true, nullable = false)
	private String code = null;

	@Column(name = "SUBJECT", length = 200, nullable = false)
	private String subject = null;

	@Column(name = "TYPE", length = 15)
	private String type = EMAIL;

	@Lob
	@Column(name = "BODY", nullable = false)
	private String body = null;

	@Column(name = "STATUS", length = 15)
	private String status = ACTIVE;

	@Column(name = "IS_HTML", length = 1)
	public String html = "Y";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Replaces message subject place holders with parameters and return
	 * 
	 * @param params
	 * @return
	 */
	public String getSubject(HashMap<String, String> params) {
		if (params == null) {
			return subject;
		}

		String text = this.subject;
		Iterator<String> it = params.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key = it.next();
			text = text.replace("{" + key + "}", params.get(key));
		}
		return text;
	}

	/**
	 * Replaces message body with params and return
	 * 
	 * @param params
	 * @return
	 */
	public String getBody(HashMap<String, String> params) {
		if (params == null) {
			return body;
		}

		String text = this.body;
		Iterator<String> it = params.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key = it.next();
			text = text.replace("{" + key + "}", params.get(key));
		}
		return text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("code", "asc");
		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		return map;
	}

	@Override
	public String getValue() {
		return code;
	}

}
