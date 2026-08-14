package com.sunilos.ctl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.DropdownList;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.form.SubjectForm;
import com.sunilos.service.CourseServiceInt;
import com.sunilos.service.SubjectServiceInt;

@RestController
@RequestMapping(value = "subject")
public class SubjectCtl extends BaseCtl<SubjectForm, SubjectDTO, SubjectServiceInt> {

	@Autowired
	private CourseServiceInt courseService;

	@GetMapping("/preload")
	public ORSResponse preload() {

		List<CourseDTO> list = courseService.search(new CourseDTO(), userContext);

		List<Map<String, Object>> courseList = list.stream()
				.map(course -> Map.<String, Object>of(
						"key", course.getKey(),
						"value", course.getValue()))
				.toList();

		Map<String, Object> preload = new HashMap<String, Object>();
		preload.put("courseList", courseList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);
		return res;

	}
}
