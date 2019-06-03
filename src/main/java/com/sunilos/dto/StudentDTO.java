package com.sunilos.dto;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Student POJO class. It is persistent object.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_STUDENT")
public class StudentDTO extends BaseDTO {

	/**
	 * Enrolment number
	 */
	@Column(name = "ENROL_NO", length = 20)
	private String enrolNo;

	/**
	 * First Name of Student
	 */
	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;
	/**
	 * Last Name of Student
	 */
	@Column(name = "LAST_NAME", length = 50)
	private String lastName;
	/**
	 * Date of Birth of Student
	 */
	@Column(name = "DOB")
	private Date dob;
	/**
	 * Mobileno of Student
	 */
	@Column(name = "MOBILE_NO", length = 15)
	private String mobileNo;

	/**
	 * Email of Student
	 */
	@Column(name = "EMAIL", length = 50)
	private String email;
	/**
	 * CollegeId of Student
	 */
	@Column(name = "COLLEGE_ID")
	private Long collegeId;
	/**
	 * College name of Student
	 */
	@Column(name = "COLLEGE_NAME", length = 50)
	private String collegeName;

	/**
	 * accessor
	 */

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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(Long collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getKey() {
		return id + "";
	}

	public String getValue() {
		return firstName + " " + lastName;
	}

	public String getEnrolNo() {
		return enrolNo;
	}

	public void setEnrolNo(String enrolNo) {
		this.enrolNo = enrolNo;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("firstName", "asc");
		map.put("lastName", "asc");
		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("enrolNo", enrolNo);
		return map;
	}

}
