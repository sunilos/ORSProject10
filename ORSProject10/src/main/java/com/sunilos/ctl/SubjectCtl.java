package com.sunilos.ctl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseReportCtl;
import com.sunilos.common.DropdownList;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.form.SubjectForm;
import com.sunilos.service.CourseServiceInt;
import com.sunilos.service.SubjectServiceInt;

@RestController
@RequestMapping(value = "subject")
public class SubjectCtl extends BaseReportCtl<SubjectForm, SubjectDTO, SubjectServiceInt> {

	@Autowired
	private CourseServiceInt courseService;

	@GetMapping("/preload")
	public ORSResponse preload() {

		List<Map<String, Object>> courseList = courseService.preloadList(new CourseDTO(), userContext);

		Map<String, Object> preload = new HashMap<String, Object>();
		preload.put("courseList", courseList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);
		return res;

	}
}
