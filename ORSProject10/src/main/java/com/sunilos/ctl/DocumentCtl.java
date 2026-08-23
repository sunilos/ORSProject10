package com.sunilos.ctl;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.common.UserContext;
import com.sunilos.dto.DocumentDTO;
import com.sunilos.form.DocumentForm;
import com.sunilos.service.DocumentServiceInt;
import com.sunilos.util.DataValidator;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST API to attach a file in application
 *
 * @author Sunil Sahu
 * @Copyright (c) SunilOS Infotech Pvt Ltd
 */
@RestController
@RequestMapping(value = "doc")
public class DocumentCtl extends BaseCtl<DocumentForm, DocumentDTO, DocumentServiceInt> {

	private static final Logger log = LoggerFactory.getLogger(DocumentCtl.class);

	@Value("${document.upload-dir}")
	String uploadDir;

	/**
	 * Replaces the file of an existing document, identified by id.
	 *
	 * @param id
	 * @param file
	 * @param description
	 * @return
	 */
	@PutMapping("/file/{id}")
	public ORSResponse updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String description) {
		return saveDocument(id, file, description, userContext);
	}

	@PostMapping("/file")
	public ORSResponse addFile(@RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String description) {
		return addFile(file, description, userContext);
	}

	/**
	 * Entry point for other controllers that call this bean directly (a plain
	 * Java method call, not an HTTP request) and already have a UserContext
	 * resolved for the current request - e.g. UserCtl uploading a profile
	 * photo. A direct call bypasses the {@code @ModelAttribute} that normally
	 * populates {@code this.userContext}, so it must be passed in explicitly.
	 */
	public ORSResponse addFile(MultipartFile file, String description, UserContext ctx) {

		System.err.println("DocumentCtl.saveDocument() called with  file="
				+ (file != null ? file.getOriginalFilename() : "null"));

		DocumentDTO doc = new DocumentDTO(file);
		doc.setDescription(description);
		doc.setPath(uploadDir);
		doc.setUserId(ctx.getUserId());

		System.err.println("DocumentCtl.saveDocument() " + 1 + doc.toString());

		long pk = baseService.save(doc, ctx);
		System.err.println("DocumentCtl.saveDocument() " + 2 + " pk=" + pk);
		return saveDocument(pk, file, description, ctx);
	}

	private ORSResponse saveDocument(Long id, MultipartFile file, String description, UserContext ctx) {
		System.err.println("DocumentCtl.saveDocument() called with id=" + id + ", file="
				+ (file != null ? file.getOriginalFilename() : "null") + ", description=" + description);
		ORSResponse response = new ORSResponse(true);

		if (file == null || file.isEmpty()) {
			response.setSuccess(false);
			response.addMessage("ERROR: No file provided");
			return response;
		}

		System.err.println("DocumentCtl" + 2);

		DocumentDTO existing = baseService.findById(id, ctx);
		if (existing == null) {
			response.setSuccess(false);
			response.addMessage("ERROR: Document not found");
			return response;
		}

		System.err.println("DocumentCtl" + 3);

		DocumentDTO doc = new DocumentDTO(file);
		doc.setId(id);
		doc.setDescription(description);
		doc.setPath(uploadDir);
		doc.setUserId(ctx.getUserId());

		System.err.println("DocumentCtl" + 4);

		try {
			saveFile(doc, file);

			System.err.println("DocumentCtl" + 5);

		} catch (IOException e) {
			log.error("Failed to store file for document id={}", id, e);
			response.setSuccess(false);
			response.addMessage("ERROR: Failed to store file");
			return response;
		}

		baseService.save(doc, ctx);

		// best-effort cleanup of the file this upload replaces
		if (existing.getName() != null && !existing.getName().equals(doc.getName())) {
			File oldFile = new File(uploadDir, existing.getName());
			if (oldFile.exists() && !oldFile.delete()) {
				log.warn("Could not delete replaced file {}", oldFile);
			}
		}

		response.setSuccess(true);
		response.addData(doc);
		return response;
	}

	private void saveFile(DocumentDTO doc, MultipartFile file) throws IOException {

		File baseDir = new File(uploadDir);
		if (!baseDir.exists() && !baseDir.mkdirs()) {
			throw new IOException("Unable to create upload directory: " + uploadDir);
		}

		File destFile = new File(baseDir, doc.getName());

		try (InputStream input = file.getInputStream(); OutputStream output = new FileOutputStream(destFile)) {
			input.transferTo(output);
		}
	}

	/**
	 * Downloads a document for the given id. Excluded from JWT auth (see
	 * {@code ORSApp} interceptor config for "/doc/pub/file/**"), so this is
	 * reachable without a Bearer token - only put data here that's fine to be
	 * publicly accessible.
	 *
	 * @param id
	 * @param response
	 */
	@GetMapping("/pub/file/{id}")
	public @ResponseBody void download(@PathVariable long id, HttpServletResponse response) {

		DocumentDTO dto = baseService.findById(id, userContext);
		if (dto == null) {
			writeError(response, HttpServletResponse.SC_NOT_FOUND, "ERROR: Document not found");
			return;
		}

		File sourceFile = new File(uploadDir, dto.getName());
		if (!sourceFile.exists()) {
			log.error("Document id={} has no file on disk at {}", id, sourceFile);
			writeError(response, HttpServletResponse.SC_NOT_FOUND, "ERROR: File not found");
			return;
		}

		response.setContentType(dto.getType());
		response.setContentLengthLong(sourceFile.length());
		if (!DataValidator.isEmptyString(dto.getOriginalName())) {
			String safeName = dto.getOriginalName().replaceAll("[\r\n\"]", "_");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + safeName + "\"");
		}

		try (InputStream input = new FileInputStream(sourceFile)) {
			input.transferTo(response.getOutputStream());
		} catch (IOException e) {
			log.error("Failed to stream document id={}", id, e);
		}
	}

	/**
	 * Entry point for other controllers that call this bean directly (a plain
	 * Java method call, not an HTTP request) and already have a UserContext
	 * resolved for the current request - see {@link #addFile(MultipartFile,
	 * String, UserContext)}.
	 */
	public void deleteDocument(long id, UserContext ctx) {
		baseService.delete(id, ctx);
	}

	@DeleteMapping("/file/{id}")
	public @ResponseBody void deleteFile(@PathVariable long id) {

		DocumentDTO dto = baseService.findById(id, userContext);
		if (dto != null) {
			File file = new File(uploadDir, dto.getName());
			if (file.exists()) {
				file.delete();
			}
		}
	}

	private void writeError(HttpServletResponse response, int status, String message) {
		response.setStatus(status);
		try {
			response.getWriter().write(message);
		} catch (IOException e) {
			log.error("Failed to write error response", e);
		}
	}

}
