package com.sunilos.dto;

import java.util.Date;
import java.util.LinkedHashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Placement POJO class. It encapsulates a Student's campus placement outcome
 * with a Company. It is persistent object.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_PLACEMENT")
public class PlacementDTO extends BaseDTO {

	public static final String STATUS_APPLIED = "Applied";
	public static final String STATUS_SHORTLISTED = "Shortlisted";
	public static final String STATUS_SELECTED = "Selected";
	public static final String STATUS_OFFER_ACCEPTED = "Offer Accepted";
	public static final String STATUS_JOINED = "Joined";
	public static final String STATUS_REJECTED = "Rejected";

	/**
	 * StudentId of Placement
	 */
	@Column(name = "STUDENT_ID")
	private Long studentId;
	/**
	 * Student name of Placement
	 */
	@Column(name = "STUDENT_NAME", length = 50)
	private String studentName;
	/**
	 * CollegeId of Placement
	 */
	@Column(name = "COLLEGE_ID")
	private Long collegeId;
	/**
	 * College name of Placement
	 */
	@Column(name = "COLLEGE_NAME", length = 50)
	private String collegeName;
	/**
	 * CompanyId of Placement
	 */
	@Column(name = "COMPANY_ID")
	private Long companyId;
	/**
	 * Company name of Placement
	 */
	@Column(name = "COMPANY_NAME", length = 50)
	private String companyName;
	/**
	 * Job title offered
	 */
	@Column(name = "JOB_TITLE", length = 50)
	private String jobTitle;
	/**
	 * Job type, e.g. Full Time, Internship
	 */
	@Column(name = "JOB_TYPE", length = 30)
	private String jobType;
	/**
	 * Package offered
	 */
	@Column(name = "PACKAGE_OFFERED")
	private Double packageOffered;
	/**
	 * Drive date
	 */
	@Column(name = "DRIVE_DATE")
	private Date driveDate;
	/**
	 * Status of placement
	 */
	@Column(name = "STATUS", length = 20)
	private String status;
	/**
	 * Offer letter number
	 */
	@Column(name = "OFFER_LETTER_NO", length = 30)
	private String offerLetterNo;
	/**
	 * Remarks
	 */
	@Column(name = "REMARKS", length = 255)
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

	public Date getDriveDate() {
		return driveDate;
	}

	public void setDriveDate(Date driveDate) {
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

	public String getKey() {
		return id + "";
	}

	public String getValue() {
		return studentName + " - " + companyName;
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("driveDate", "desc");
		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		return null;
	}

}
