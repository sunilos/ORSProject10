package com.sunilos.dto;

import java.util.LinkedHashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Subject POJO class. It is persistent object.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_SUBJECT")
public class SubjectDTO extends BaseDTO {

	/**
	 * Name of Subject
	 */
	@Column(name = "NAME", length = 50)
	private String name;
	/**
	 * Description of Subject
	 */
	@Column(name = "DESCRIPTION", length = 255)
	private String description;
	/**
	 * CourseId of Subject
	 */
	@Column(name = "COURSE_ID")
	private Long courseId;
	/**
	 * Course name of Subject
	 */
	@Column(name = "COURSE_NAME", length = 50)
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

	public String getKey() {
		return id + "";
	}

	public String getValue() {
		return name;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("name", "asc");
		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", name);
		return map;
	}

}
