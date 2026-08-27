package com.sunilos.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.TimeTableDTO;

/**
 * Contains TimeTable form elements and their declarative input validations.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class TimeTableForm extends BaseForm {

	@NotNull
	private java.util.Date examDate;

	@NotEmpty
	private String examTime;

	@NotEmpty
	private String semester;

	@NotNull
	private Long courseId;

	private String courseName;

	@NotNull
	private Long subjectId;

	private String subjectName;

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
	public BaseDTO getDto() {
		TimeTableDTO dto = initDTO(new TimeTableDTO());
		dto.setExamDate(examDate);
		dto.setExamTime(examTime);
		dto.setSemester(semester);
		dto.setCourseId(courseId);
		dto.setCourseName(courseName);
		dto.setSubjectId(subjectId);
		dto.setSubjectName(subjectName);
		return dto;
	}

}
