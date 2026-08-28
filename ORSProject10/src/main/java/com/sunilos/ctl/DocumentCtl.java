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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
		try {
			DocumentDTO doc = baseService.update(id, file, description, userContext);
			return new ORSResponse(true, "document added", doc);
		} catch (Exception e) {
			return new ORSResponse(false, "document fail", doc);
		}
	}

	@PostMapping("/file")
	public ORSResponse addFile(@RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String description) {
		try {
			DocumentDTO doc = baseService.add(file, description, userContext);
			return new ORSResponse(true, "document added", doc);
		} catch (Exception e) {
			return new ORSResponse(false, "document fail", doc);
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

		Path sourceFile = Paths.get(uploadDir, dto.getName());
		if (!Files.exists(sourceFile)) {
			log.error("Document id={} has no file on disk at {}", id, sourceFile);
			writeError(response, HttpServletResponse.SC_NOT_FOUND, "ERROR: File not found");
			return;
		}

		try {
			response.setContentType(dto.getType());
			response.setContentLengthLong(Files.size(sourceFile));
			if (!DataValidator.isEmptyString(dto.getOriginalName())) {
				String safeName = dto.getOriginalName().replaceAll("[\r\n\"]", "_");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + safeName + "\"");
			}

			Files.copy(sourceFile, response.getOutputStream());
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
		System.out.println("received Image id " + id);
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
