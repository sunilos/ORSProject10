package com.sunilos.form;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.DocumentDTO;

/**
 * Contains Document form elements and their declarative input validations.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class DocumentForm extends BaseForm {

	private String name;

	private String type;

	private String description;

	private String tags;

	private String path;

	private Long userId;

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
	public BaseDTO getDto() {
		DocumentDTO dto = initDTO(new DocumentDTO());
		dto.setName(name);
		dto.setType(type);
		dto.setDescription(description);
		dto.setTags(tags);
		dto.setPath(path);
		dto.setUserId(userId);
		return dto;
	}

}