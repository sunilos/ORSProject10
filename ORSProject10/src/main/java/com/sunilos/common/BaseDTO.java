package com.sunilos.common;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Base class extended by all DTOs.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@MappedSuperclass
public abstract class BaseDTO implements Serializable, DropdownList, Comparable<BaseDTO> {

	/**
	 * Non Business primary key
	 */
	@Id
	@GeneratedValue(generator = "ncsPk")
	@GenericGenerator(name = "ncsPk", strategy = "native")
	@Column(name = "ID", unique = true, nullable = false)
	protected Long id;
	/**
	 * Contains USER ID who created this database record
	 */
	@Column(name = "CREATED_BY", length = 50)
	protected String createdBy = "root";
	/**
	 * Contains USER ID who modified this database record
	 */
	@Column(name = "MODIFIED_BY", length = 50)
	protected String modifiedBy = "root";
	/**
	 * Contains Created Timestamp of database record
	 */
	@CreatedDate
	@Column(name = "CREATED_DATETIME")
	protected Timestamp createdDatetime;
	/**
	 * Contains Modified Timestamp of database record
	 */
	@LastModifiedDate
	@Column(name = "MODIFIED_DATETIME")
	protected Timestamp modifiedDatetime;

	@Column(name = "ORG_ID")
	protected Long orgId = 0L;

	@Column(name = "ORG_NAME", length = 50)
	private String orgName;

	/**
	 * accessor
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Timestamp l) {
		this.createdDatetime = l;
	}

	public Timestamp getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Timestamp modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public int compareTo(BaseDTO next) {
		return getValue().compareTo(next.getValue());
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getKey() {
		return String.valueOf(id);
	}

	/**
	 * Order by attributes
	 * 
	 * @return
	 */
	public abstract LinkedHashMap<String, String> orderBY();

	/**
	 * Unique attributes
	 * 
	 * @return
	 */
	public abstract LinkedHashMap<String, Object> uniqueKeys();

	/**
	 * Apply organization filter
	 * 
	 * @return
	 */
	public boolean isGroupFilter() {
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Method[] ms = this.getClass().getMethods();

		String mName = null;
		for (int i = 0; i < ms.length; i++) {
			mName = ms[i].getName();
			if (mName.startsWith("get")) {
				try {
					buffer.append("\n\t" + mName + " = " + ms[i].invoke(this, (Object[]) null));
				} catch (Exception e) {
				}
			}

		}
		return buffer.toString();
	}

}