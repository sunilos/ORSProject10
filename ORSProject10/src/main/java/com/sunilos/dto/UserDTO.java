package com.sunilos.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * User POJO class. It is persistent object.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_USER")
public class UserDTO extends BaseDTO {

	public static final String ACTIVE = "Activate";
	public static final String DEACTIVE = "deactivate";
	public static final String LOCKED = "locked";

	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;

	@Column(name = "LAST_NAME", length = 50)
	private String lastName;

	@Column(name = "LOGIN_ID", length = 50)
	private String loginId;

	@Column(name = "PASSWORD", length = 50)
	private String password;

	@Column(name = "EMAIL", length = 50)
	private String email;

	@Column(name = "STATUS", length = 20)
	private String status;

	@Column(name = "ROLE_NAME", length = 50)
	private String roleName = null;

	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "PHONE", length = 50)
	private String phone;

	@Column(name = "ALTERNATE_MOBILE", length = 50)
	private String alternateMobile;

	@Column(name = "DOB")
	private Date dob;

	@Column(name = "GENDER", length = 10)
	private String gender;

	@Column(name = "IMAGE_ID")
	private Long imageId = new Long(0);

	/**
	 * Last login date and time
	 */
	@Column(name = "LAST_LOGIN")
	private Timestamp lastLogin;

	/**
	 * How many time user has entered incorrect password
	 */
	@Column(name = "UNSUCCESS_LOGIN")
	private Integer unsucessfullLoginAttempt = 0;

	/**
	 * User status will be active after this date
	 */
	@Column(name = "VALID_FROM_DATE")
	private Date validFromDate;

	/**
	 * User will be deactivted after this date
	 */
	@Column(name = "VALID_TO_DATE")
	private Date validToDate;

	/**
	 * User can login only after this time
	 */
	@Column(name = "ACCESS_TIME_FROM")
	private Time accessTimeFrom;

	/**
	 * User can login only before this time
	 */
	@Column(name = "ACCESS_TIME_TO")
	private Time accessTimeTo;

	public String getFirstName() {
		return firstName;
	}

	public String getName() {
		return firstName + " " + lastName;
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

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getUnsucessfullLoginAttempt() {
		return unsucessfullLoginAttempt;
	}

	public void setUnsucessfullLoginAttempt(Integer unsucessfullLoginAttempt) {
		this.unsucessfullLoginAttempt = unsucessfullLoginAttempt;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	public Time getAccessTimeFrom() {
		return accessTimeFrom;
	}

	public void setAccessTimeFrom(Time accessTimeFrom) {
		this.accessTimeFrom = accessTimeFrom;
	}

	public Time getAccessTimeTo() {
		return accessTimeTo;
	}

	public void setAccessTimeTo(Time accessTimeTo) {
		this.accessTimeTo = accessTimeTo;
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
		map.put("loginId", loginId);
		return map;
	}

}
