package com.sunilos.form;

import jakarta.validation.constraints.NotEmpty;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.RoleDTO;

/**
 * Contains Role form elements and their declarative input validations.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class RoleForm extends BaseForm {

	@NotEmpty
	private String name = null;

	@NotEmpty
	private String description = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public BaseDTO getDto() {
		RoleDTO dto = initDTO(new RoleDTO());
		dto.setName(name);
		dto.setDescription(description);
		return dto;
	}

	@Override
	public void populate(BaseDTO bdDto) {
	}
}
