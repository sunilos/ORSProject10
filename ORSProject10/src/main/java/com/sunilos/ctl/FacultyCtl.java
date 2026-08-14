package com.sunilos.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.FacultyDTO;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.form.FacultyForm;
import com.sunilos.service.CollegeServiceInt;
import com.sunilos.service.CourseServiceInt;
import com.sunilos.service.FacultyServiceInt;
import com.sunilos.service.SubjectServiceInt;

@RestController
@RequestMapping(value = "faculty")
public class FacultyCtl extends BaseCtl<FacultyForm, FacultyDTO, FacultyServiceInt> {

	@Autowired
	private CollegeServiceInt collegeService;

	@Autowired
	private CourseServiceInt courseService;

	@Autowired
	private SubjectServiceInt subjectService;

	@GetMapping("/preload")
	public ORSResponse preload() {
		List<CollegeDTO> list = collegeService.search(new CollegeDTO(), userContext);

		List<Map<String, Object>> collegeList = list.stream()
				.map(college -> Map.<String, Object>of(
						"key", college.getKey(),
						"value", college.getValue()))
				.toList();

		Map<String, Object> preload = new HashMap<String, Object>();
		preload.put("collegeList", collegeList);

		List<CourseDTO> clist = courseService.search(new CourseDTO(), userContext);

		List<Map<String, Object>> courseList = clist.stream()
				.map(course -> Map.<String, Object>of(
						"key", course.getKey(),
						"value", course.getValue()))
				.toList();

		preload.put("courseList", courseList);

		List<SubjectDTO> slist = subjectService.search(new SubjectDTO(), userContext);

		List<Map<String, Object>> subjectList = slist.stream()
				.map(subject -> Map.<String, Object>of(
						"key", subject.getKey(),
						"value", subject.getValue()))
				.toList();

		preload.put("subjectList", subjectList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);
		return res;

	}
}
