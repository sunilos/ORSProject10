package com.sunilos.form;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.UserDTO;

/**
 * Contains User form elements and their declarative input validations.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class UserForm extends BaseForm {

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	private String loginId;

	private String password;

	private String email;

	private String status;

	private String roleName = null;

	@NotNull
	@Min(1)
	private Long roleId;

	private String phone;

	private String alternateMobile;

	private Date dob;

	private String gender;

	private String imagePath;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAlternateMobile() {
		return alternateMobile;
	}

	public void setAlternateMobile(String alternateMobile) {
		this.alternateMobile = alternateMobile;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public void setName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public BaseDTO getDto() {

		UserDTO dto = initDTO(new UserDTO());
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setEmail(email);
		dto.setLoginId(loginId);
		dto.setPassword(password);
		dto.setRoleId(roleId);
		dto.setRoleName(roleName);
		dto.setCreatedBy(alternateMobile);
		dto.setDob(dob);
		dto.setGender(gender);
		dto.setPhone(phone);
		dto.setStatus(status);
		return dto;
	}
	
	

}
