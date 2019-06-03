package com.sunilos.common.attachment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;

@RestController
@RequestMapping(value = "Attachment")

/**
 * REST API to attach a file in application
 * 
 * @author Sunil Sahu
 * @Copyright (c) SunilOS Infotech Pvt Ltd
 *
 */
public class AttachmentCtl extends BaseCtl<AttachmentForm, AttachmentDTO, AttachmentServiceInt> {

	/**
	 * Uploads a file. If ID is exist in database then document is updated else
	 * added to database.
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	public Map<String, Object> upload(@RequestParam(required = false) Long id, @RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String description, HttpServletRequest req) {

		System.out.println("Received ID " + id);

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", true);

		AttachmentDTO doc = new AttachmentDTO(file);
		doc.setId(id);
		doc.setDescription(description);
		doc.setPath(req.getServletPath());
		doc.setUserId(userContext.getUserId());

		Long pk = baseService.save(doc, userContext);

		Map<String, Object> map = new HashMap<String, Object>();
		response.put("id", pk);
		response.put("name", doc.getName());
		response.put("type", doc.getType());
		response.put("size", file.getSize());

		return response;
	}

	/**
	 * Download a document for given id
	 * 
	 * @param id
	 * @param response
	 */
	@GetMapping("/download/{id}")
	public @ResponseBody void download(@PathVariable long id, HttpServletResponse response) {

		AttachmentDTO dto = baseService.findById(id, userContext);

		try {
			if (dto != null) {
				response.setContentType(dto.getType());
				OutputStream out = response.getOutputStream();
				out.write(dto.getDoc());
				out.close();
			} else {
				response.getWriter().write("ERROR: File not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ORSResponse search(@RequestBody AttachmentForm form) {

		System.out.println(form.getName() + " Page size is***** 88 -------yy-----------***********");

		AttachmentSummaryDTO dto = new AttachmentSummaryDTO();

		dto.setId(form.getId());
		dto.setName(form.getName());
		dto.setType(form.getType());
		dto.setTags(form.getTags());
		dto.setUserId(form.getUserId());
		dto.setPath(form.getPath());

		ORSResponse res = new ORSResponse(true);

		res.addData(baseService.search(dto, userContext));

		return res;

	}

	@Override
	public ORSResponse search(@RequestBody AttachmentForm form, int pageNo) {
		// 0 is first page index
		pageNo = (pageNo < 0) ? 0 : pageNo;

		AttachmentSummaryDTO dto = new AttachmentSummaryDTO();
		dto.setId(form.getId());
		dto.setName(form.getName());
		dto.setType(form.getType());
		dto.setTags(form.getTags());
		dto.setUserId(form.getUserId());
		dto.setPath(form.getPath());

		ORSResponse res = new ORSResponse(true);

		System.out.println(form.getName() + " Page size is***** 88 ------------------***********");

		res.addData(baseService.search(dto, pageNo, 5, userContext));

		return res;
	}

}
