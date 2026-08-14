package com.sunilos.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.SubjectDTO;

/**
 * Contains Subject form elements and their declarative input validations.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class SubjectForm extends BaseForm {

	@NotEmpty
	private String name;

	@NotEmpty
	private String description;

	@NotNull
	private Long courseId;

	private String courseName;

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

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
	public BaseDTO getDto() {
		SubjectDTO dto = initDTO(new SubjectDTO());
		dto.setName(name);
		dto.setDescription(description);
		dto.setCourseId(courseId);
		dto.setCourseName(courseName);
		return dto;
	}

}
