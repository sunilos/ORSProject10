package com.sunilos.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.CollegeDTO;

/**
 * Contains College form elements and their declarative input validations.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class CollegeForm extends BaseForm {

	@NotEmpty
	private String name;

	@NotEmpty
	private String address;

	@NotEmpty
	private String state;

	@NotEmpty
	private String city;

	@NotNull
	@Pattern(regexp = "\\d{10}")
	private String phoneNo;

	private String message;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public BaseDTO getDto() {
		CollegeDTO dto = initDTO(new CollegeDTO());
		dto.setAddress(address);
		dto.setCity(city);
		dto.setName(name);
		dto.setState(state);
		dto.setPhoneNo(phoneNo);
		return dto;
	}

}
