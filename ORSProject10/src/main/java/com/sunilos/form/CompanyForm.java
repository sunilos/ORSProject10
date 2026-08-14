package com.sunilos.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.BaseForm;
import com.sunilos.dto.CompanyDTO;

/**
 * Contains Company form elements and their declarative input validations.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class CompanyForm extends BaseForm {

	@NotEmpty
	private String name;

	@NotEmpty
	private String industry;

	private String website;

	@NotEmpty
	private String contactPerson;

	@NotEmpty
	@Email
	private String contactEmail;

	@NotEmpty
	@Pattern(regexp = "\\d{10}")
	private String contactPhone;

	@NotEmpty
	private String city;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public BaseDTO getDto() {
		CompanyDTO dto = initDTO(new CompanyDTO());
		dto.setName(name);
		dto.setIndustry(industry);
		dto.setWebsite(website);
		dto.setContactPerson(contactPerson);
		dto.setContactEmail(contactEmail);
		dto.setContactPhone(contactPhone);
		dto.setCity(city);
		return dto;
	}

}
