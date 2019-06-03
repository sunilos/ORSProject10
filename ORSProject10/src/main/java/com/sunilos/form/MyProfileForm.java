package com.sunilos.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Contains MyProfile for elements and its declarative validation
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS *
 */

public class MyProfileForm {

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	private String login;

	@NotEmpty
	private String gender;

	@Pattern(regexp = "\\d{10}", message = "{Pattern.form.phoneNo}")
	private String mobileNo;

	private Date dob;

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}
