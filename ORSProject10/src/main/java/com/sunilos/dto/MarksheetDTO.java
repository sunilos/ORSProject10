package com.sunilos.dto;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Marksheet POJO class. It is persistent object.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_MARKSHEET")
public class MarksheetDTO extends BaseDTO {

	@Column(name = "ROLL_NO", length = 20)
	protected String rollNo = null;

	@Column(name = "NAME", length = 50)
	protected String name = null;

	@Column(name = "PHYSICS")
	protected Integer physics;

	@Column(name = "CHEMISTRY")
	protected Integer chemistry;

	@Column(name = "MATHS")
	protected Integer maths;

	@Column(name = "STUDENT_ID")
	protected Long studentId;
	
	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

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

	public String getKey() {
		return id + "";
	}

	public String getValue() {
		return rollNo;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
		hashMap.put("rollNo", "asc");
		return hashMap;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		return null;
	}

}