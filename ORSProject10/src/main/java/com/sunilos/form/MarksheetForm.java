package com.sunilos.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.common.message.MessageDTO;
import com.sunilos.dto.MarksheetDTO;

/**
 * Contains Marksheet form elements and their declarative input validations.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class MarksheetForm extends BaseForm {

	@NotEmpty
	private String rollNo = null;

	@NotNull
	protected Long studentId;

	private String name = null;

	@NotNull
	@Min(0)
	@Max(100)
	private Integer physics;

	@NotNull
	@Min(0)
	@Max(100)
	private Integer chemistry;

	@NotNull
	@Min(0)
	@Max(100)
	protected Integer maths;

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPhysics() {
		return physics;
	}

	public void setPhysics(Integer physics) {
		this.physics = physics;
	}

	public Integer getChemistry() {
		return chemistry;
	}

	public void setChemistry(Integer chemistry) {
		this.chemistry = chemistry;
	}

	public Integer getMaths() {
		return maths;
	}

	public void setMaths(Integer maths) {
		this.maths = maths;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	@Override
	public BaseDTO getDto() {
		MarksheetDTO dto = initDTO(new MarksheetDTO());
		dto.setRollNo(rollNo);
		dto.setStudentId(studentId);
		dto.setName(name);
		dto.setPhysics(physics);
		dto.setChemistry(chemistry);
		dto.setMaths(maths);
		return dto;
	}

}
