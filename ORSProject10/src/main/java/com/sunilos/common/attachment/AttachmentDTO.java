package com.sunilos.common.attachment;

import java.io.IOException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

/**
 * Contains attached file information and data
 * 
 * @author Sunil Sahu
 * @Copyright (c) SunilOS Infotech Pvt Ltd
 */

@Entity
@Table(name = "RT_ATTACHMENT")

public class AttachmentDTO extends AttachmentBaseDTO {

	public AttachmentDTO() {
		super();
	}

	public AttachmentDTO(MultipartFile file) {
		name = file.getOriginalFilename();
		type = file.getContentType();
		try {
			doc = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Contaisn file data
	 */
	@Lob
	@Column(name = "DOC")
	private byte[] doc;

	public byte[] getDoc() {
		return doc;
	}

	public void setDoc(byte[] doc) {
		this.doc = doc;
	}

}