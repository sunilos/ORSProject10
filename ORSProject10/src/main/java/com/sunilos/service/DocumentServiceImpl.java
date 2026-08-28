package com.sunilos.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sunilos.common.BaseServiceImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dao.DocumentDAOInt;
import com.sunilos.dto.DocumentDTO;
import com.sunilos.exception.DuplicateRecordException;
import com.sunilos.util.DataValidator;

/**
 * Session facade of Document Service. It is transactional, apply declarative
 * transactions with help of Spring AOP.
 *
 * If unchecked exception is propagated from a method then transaction is
 * rolled back.
 *
 * Default propagation value is Propagation.REQUIRED and readOnly = false
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@Service
@Transactional
public class DocumentServiceImpl extends BaseServiceImpl<DocumentDTO, DocumentDAOInt> implements DocumentServiceInt {

	private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Value("${document.upload-dir}")
	private String uploadDir;

	private void setFileName(DocumentDTO doc, MultipartFile file) {
		String originalName = file.getOriginalFilename();
		String type = file.getContentType();

		String name;
		int lastDot = DataValidator.isEmptyString(originalName) ? -1 : originalName.lastIndexOf('.');
		if (lastDot == -1 || lastDot == originalName.length() - 1) {
			name = UUID.randomUUID().toString();
		} else {
			String fileExtension = originalName.substring(lastDot + 1).toLowerCase();
			name = UUID.randomUUID().toString() + "." + fileExtension;
		}

		doc.setName(name);
		doc.setOriginalName(originalName);
		doc.setType(type);
	}

	public DocumentDTO add(MultipartFile file, String description, UserContext userContext)
			throws DuplicateRecordException, IOException {
		return this.update(null, file, description, userContext);
	}

	public DocumentDTO update(Long id, MultipartFile file, String description, UserContext userContext)
			throws DuplicateRecordException, IOException {

		DocumentDTO doc;
		String oldFileName = null;
		if (id != null && id > 0) {
			doc = baseDao.findByPK(id, userContext);
			oldFileName = doc.getName();
		} else {
			doc = new DocumentDTO();
		}
		doc.setDescription(description);
		doc.setPath(uploadDir);
		doc.setUserId(userContext.getUserId());
		setFileName(doc, file);

		id = this.save(doc, userContext);

		saveFile(doc, file);
		if (oldFileName != null) {
			deleteFile(oldFileName);
		}

		doc.setId(id);
		return doc;
	}

	private void saveFile(DocumentDTO doc, MultipartFile file) throws IOException {

		Path baseDir = Paths.get(uploadDir);
		Files.createDirectories(baseDir);

		Path destFile = baseDir.resolve(doc.getName());

		try (InputStream input = file.getInputStream()) {
			Files.copy(input, destFile, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private void deleteFile(String fileName) throws IOException {

		Path baseDir = Paths.get(uploadDir);
		Path destFile = baseDir.resolve(fileName);

		boolean deleted = Files.deleteIfExists(destFile);

		if (!deleted) {
			log.warn("File does not exist: {}", fileName);
		}
	}

}
