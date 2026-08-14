package com.sunilos.form;

import jakarta.validation.constraints.NotEmpty;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.CourseDTO;

/**
 * Contains Course form elements and their declarative input validations.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class CourseForm extends BaseForm {

	@NotEmpty
	private String name;

	@NotEmpty
	private String description;

	@NotEmpty
	private String duration;

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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public BaseDTO getDto() {
		CourseDTO dto = initDTO(new CourseDTO());
		dto.setName(name);
		dto.setDescription(description);
		dto.setDuration(duration);
		return dto;
	}

}
