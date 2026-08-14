package com.sunilos.dto;

import java.util.LinkedHashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.sunilos.common.BaseDTO;

/**
 * Company POJO class. It is persistent object.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@Entity
@Table(name = "RT_COMPANY")
public class CompanyDTO extends BaseDTO {

	/**
	 * Name of Company
	 */
	@Column(name = "NAME", length = 50)
	private String name;
	/**
	 * Industry of Company
	 */
	@Column(name = "INDUSTRY", length = 50)
	private String industry;
	/**
	 * Website of Company
	 */
	@Column(name = "WEBSITE", length = 100)
	private String website;
	/**
	 * Contact person of Company
	 */
	@Column(name = "CONTACT_PERSON", length = 50)
	private String contactPerson;
	/**
	 * Contact email of Company
	 */
	@Column(name = "CONTACT_EMAIL", length = 50)
	private String contactEmail;
	/**
	 * Contact phone of Company
	 */
	@Column(name = "CONTACT_PHONE", length = 15)
	private String contactPhone;
	/**
	 * City of Company
	 */
	@Column(name = "CITY", length = 50)
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
