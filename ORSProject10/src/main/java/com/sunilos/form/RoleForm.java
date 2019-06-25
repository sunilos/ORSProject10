package com.sunilos.form;

import javax.validation.constraints.NotEmpty;

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
	private String discription = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	@Override
	public BaseDTO getDto() {
		RoleDTO dto = initDTO(new RoleDTO());
		dto.setName(name);
		dto.setDiscription(discription);
		return dto;
	}

	@Override
	public void populate(BaseDTO bdDto) {
	}
}
