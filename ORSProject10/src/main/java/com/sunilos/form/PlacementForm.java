package com.sunilos.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.PlacementDTO;
import com.sunilos.util.Util;

/**
 * Contains Placement form elements and their declarative input validations.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class PlacementForm extends BaseForm {

	@NotNull
	private Long studentId;

	private String studentName;

	@NotNull
	private Long collegeId;

	private String collegeName;

	@NotNull
	private Long companyId;

	private String companyName;

	@NotEmpty
	private String jobTitle;

	@NotEmpty
	private String jobType;

	@NotNull
	private Double packageOffered;

	private String driveDate;

	@NotEmpty
	private String status;

	private String offerLetterNo;

	private String remarks;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
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

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Double getPackageOffered() {
		return packageOffered;
	}

	public void setPackageOffered(Double packageOffered) {
		this.packageOffered = packageOffered;
	}

	public String getDriveDate() {
		return driveDate;
	}

	public void setDriveDate(String driveDate) {
		this.driveDate = driveDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOfferLetterNo() {
		return offerLetterNo;
	}

	public void setOfferLetterNo(String offerLetterNo) {
		this.offerLetterNo = offerLetterNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public BaseDTO getDto() {
		PlacementDTO dto = initDTO(new PlacementDTO());
		dto.setStudentId(studentId);
		dto.setStudentName(studentName);
		dto.setCollegeId(collegeId);
		dto.setCollegeName(collegeName);
		dto.setCompanyId(companyId);
		dto.setCompanyName(companyName);
		dto.setJobTitle(jobTitle);
		dto.setJobType(jobType);
		dto.setPackageOffered(packageOffered);
		dto.setDriveDate(Util.convertDateFormat(driveDate));
		dto.setStatus(status);
		dto.setOfferLetterNo(offerLetterNo);
		dto.setRemarks(remarks);
		return dto;
	}

}
