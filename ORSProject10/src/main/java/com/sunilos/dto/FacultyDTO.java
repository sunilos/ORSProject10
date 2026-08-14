package com.sunilos.dto;

import java.util.Date;
import java.util.LinkedHashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Faculty POJO class. It is persistent object.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_FACULTY")
public class FacultyDTO extends BaseDTO {

	/**
	 * CollegeId of Faculty
	 */
	@Column(name = "COLLEGE_ID")
	private Long collegeId;
	/**
	 * College name of Faculty
	 */
	@Column(name = "COLLEGE_NAME", length = 50)
	private String collegeName;
	/**
	 * First Name of Faculty
	 */
	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;
	/**
	 * Last Name of Faculty
	 */
	@Column(name = "LAST_NAME", length = 50)
	private String lastName;
	/**
	 * Email of Faculty
	 */
	@Column(name = "EMAIL", length = 50)
	private String email;
	/**
	 * Mobileno of Faculty
	 */
	@Column(name = "MOBILE_NO", length = 15)
	private String mobileNo;
	/**
	 * Address of Faculty
	 */
	@Column(name = "ADDRESS", length = 50)
	private String address;
	/**
	 * Gender of Faculty
	 */
	@Column(name = "GENDER", length = 10)
	private String gender;
	/**
	 * Date of Birth of Faculty
	 */
	@Column(name = "DOB")
	private Date dob;
	/**
	 * CourseId of Faculty
	 */
	@Column(name = "COURSE_ID")
	private Long courseId;
	/**
	 * Course name of Faculty
	 */
	@Column(name = "COURSE_NAME", length = 50)
	private String courseName;
	/**
	 * SubjectId of Faculty
	 */
	@Column(name = "SUBJECT_ID")
	private Long subjectId;
	/**
	 * Subject name of Faculty
	 */
	@Column(name = "SUBJECT_NAME", length = 50)
	private String subjectName;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getKey() {
		return id + "";
	}

	public String getValue() {
		return firstName + " " + lastName;
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
		map.put("email", email);
		return map;
	}

}
