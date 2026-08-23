package com.sunilos.dto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import com.sunilos.common.BaseDTO;
import com.sunilos.common.ORSResponse;
import com.sunilos.util.DataValidator;

/**
 * Contains attached file information and data
 *
 * @author Sunil Sahu
 * @Copyright (c) SunilOS Infotech Pvt Ltd
 */

@Entity
@Table(name = "RT_DOCUMENT")
public class DocumentDTO extends BaseDTO {

	public DocumentDTO() {
		super();
	}

	public DocumentDTO(MultipartFile file) {
		originalName = file.getOriginalFilename();
		type = file.getContentType();
		int lastDot = DataValidator.isEmptyString(originalName) ? -1 : originalName.lastIndexOf('.');
		if (lastDot == -1 || lastDot == originalName.length() - 1) {
			name = UUID.randomUUID().toString();
		} else {
			String fileExtension = originalName.substring(lastDot + 1).toLowerCase();
			name = UUID.randomUUID().toString() + "." + fileExtension;
		}
	}

	/***
	 * 
	 * Contains name
	 * of file
	 */
	@Column(name = "NAME", length = 200)
	private String name;

	@Column(name = "ORIGINAL_NAME", length = 200)
	private String originalName;

	/**
	 * Contains MIME type of file
	 */
	@Column(name = "TYPE", length = 100)
	private String type;

	/**
	 * Contains file description
	 */
	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	/**
	 * Contains tags to be searched
	 */
	@Column(name = "TAGS", length = 500)
	private String tags;

	/**
	 * Contains PATH of controller
	 */
	@Column(name = "PATH", length = 500)
	private String path;

	/**
	 * Contains user id who uploaded this file
	 */
	@Column(name = "USER_ID")
	private Long userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getKey() {
		return id + "";
	}

	@Override
	public String getValue() {
		return name + "(" + type + ")";
	}

	@Override
	public LinkedHashMap<String, String> orderBY() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("name", "asc");
		return map;
	}

	@Override
	public LinkedHashMap<String, Object> uniqueKeys() {
		return new LinkedHashMap<String, Object>();
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}