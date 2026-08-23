package com.sunilos.ctl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseReportCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CourseDTO;
import com.sunilos.form.CourseForm;
import com.sunilos.service.CourseServiceInt;

@RestController
@RequestMapping(value = "course")
public class CourseCtl extends BaseReportCtl<CourseForm, CourseDTO, CourseServiceInt> {

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		return res;
	}
}
