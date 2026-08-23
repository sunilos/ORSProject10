package com.sunilos.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sunilos.common.BaseReportCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.DocumentDTO;
import com.sunilos.dto.FacultyDTO;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.form.FacultyForm;
import com.sunilos.service.CollegeServiceInt;
import com.sunilos.service.CourseServiceInt;
import com.sunilos.service.FacultyServiceInt;
import com.sunilos.service.SubjectServiceInt;

@RestController
@RequestMapping(value = "faculty")
public class FacultyCtl extends BaseReportCtl<FacultyForm, FacultyDTO, FacultyServiceInt> {

	private static final Logger log = LoggerFactory.getLogger(FacultyCtl.class);

	@Autowired
	private CollegeServiceInt collegeService;

	@Autowired
	private CourseServiceInt courseService;

	@Autowired
	private SubjectServiceInt subjectService;

	@Autowired
	private DocumentCtl documentCtl;

	@GetMapping("/preload")
	public ORSResponse preload() {

		List<Map<String, Object>> collegeList = collegeService.preloadList(new CollegeDTO(), userContext);

		Map<String, Object> preload = new HashMap<String, Object>();
		preload.put("collegeList", collegeList);

		List<Map<String, Object>> courseList = courseService.preloadList(new CourseDTO(), userContext);

		preload.put("courseList", courseList);

		List<Map<String, Object>> subjectList = subjectService.preloadList(new SubjectDTO(), userContext);

		preload.put("subjectList", subjectList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);
		return res;

	}

	/**
	 * Uploads profile picture of given faculty id
	 *
	 * @param facultyId
	 * @param file
	 * @return
	 */
	@PutMapping("/profilePhoto/{facultyId}")
	public ORSResponse uploadFacultyProfilePhoto(@PathVariable Long facultyId,
			@RequestParam("file") MultipartFile file) {

		if (file == null || file.isEmpty()) {
			return new ORSResponse(false, "No file provided");
		}

		FacultyDTO facultyDTO = baseService.findById(facultyId, userContext);
		if (facultyDTO == null) {
			return new ORSResponse(false, "Faculty not found");
		}

		long oldImageId = facultyDTO.getImageId() == null ? 0L : facultyDTO.getImageId();

		ORSResponse docResponse = documentCtl.addFile(file, facultyDTO.getValue() + " faculty profile photo",
				userContext);
		if (!docResponse.isSuccess()) {
			return new ORSResponse(false, docResponse.getMessage());
		}

		DocumentDTO uploadedDocumentDTO = docResponse.getData(DocumentDTO.class);
		facultyDTO.setImageId(uploadedDocumentDTO.getId());
		baseService.save(facultyDTO, userContext);

		if (oldImageId > 0) {
			try {
				documentCtl.deleteDocument(oldImageId, userContext);
			} catch (Exception e) {
				log.warn("Failed to delete old profile photo (document id={}) for faculty id={}", oldImageId,
						facultyId, e);
			}
		}

		return new ORSResponse(true, "Profile photo uploaded successfully");
	}
}
