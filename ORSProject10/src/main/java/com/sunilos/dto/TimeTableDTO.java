package com.sunilos.dto;

import java.util.LinkedHashMap;

import com.sunilos.common.BaseDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TimeTable POJO class. It is persistent object.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_TIME_TABLE")
public class TimeTableDTO extends BaseDTO {

	@Column(name = "EXAM_DATE")
	private java.util.Date examDate = null;

	@Column(name = "EXAM_TIME", length = 20)
	private String examTime = null;

	@Column(name = "SEMESTER", length = 50)
	private String semester = null;

	@Column(name = "COURSE_ID")
	private Long courseId = null;

	/**
	 * Denormalized Course Name
	 */
	@Column(name = "COURSE_NAME", length = 50)
	private String courseName = null;

	@Column(name = "SUBJECT_ID")
	private Long subjectId = null;

	/**
	 * Denormalized Subject Name
	 */
	@Column(name = "SUBJECT_NAME", length = 50)
	private String subjectName = null;

	public java.util.Date getExamDate() {
		return examDate;
	}

	public void setExamDate(java.util.Date examDate) {
		this.examDate = examDate;
	}

	public String getExamTime() {
		return examTime;
	}

	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
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

	@Override
	public String getKey() {
		return id + "";
	}

	@Override
	public String getValue() {
		return subjectName;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		map.put("examDate", "asc");
		map.put("examTime", "asc");

		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		// Same subject ka same semester aur same date/time par
		// duplicate exam schedule nahi hoga
		map.put("courseId", courseId);
		map.put("subjectId", subjectId);
		map.put("semester", semester);
		map.put("examDate", examDate);
		map.put("examTime", examTime);

		return map;
	}
}