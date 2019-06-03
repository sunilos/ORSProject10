package com.sunilos.common.attachment;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.sunilos.common.BaseDTO;

/**
 * Contains attached file information and data
 * 
 * @author Sunil Sahu
 * @Copyright (c) SunilOS Infotech Pvt Ltd
 */

@MappedSuperclass
public class AttachmentBaseDTO extends BaseDTO {

	/**
	 * Contains name of file
	 */
	@Column(name = "NAME", length = 100)
	protected String name = null;

	/**
	 * Contains MIME type of file
	 */
	@Column(name = "TYPE", length = 100)
	protected String type = null;

	/**
	 * Contains file description
	 */
	@Column(name = "DESCRIPTION", length = 500)
	protected String description = null;

	/**
	 * Contains tags to be searched
	 */
	@Column(name = "TAGS", length = 500)
	protected String tags = null;

	/**
	 * Contains PATH of controller
	 */
	@Column(name = "PATH", length = 500)
	protected String path = null;

	/**
	 * Contains use id who uploaded this file
	 */
	@Column(name = "USER_ID")
	protected Long userId = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getValue() {
		return name + "(" + type + ")";
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("name", "asc");
		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		// map.put("name", name);
		return map;
	}

}