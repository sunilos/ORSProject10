package com.sunilos.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseReportCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CourseDTO;
import com.sunilos.dto.SubjectDTO;
import com.sunilos.dto.TimeTableDTO;
import com.sunilos.form.TimeTableForm;
import com.sunilos.service.CourseServiceInt;
import com.sunilos.service.SubjectServiceInt;
import com.sunilos.service.TimeTableServiceInt;

@RestController
@RequestMapping(value = "timetable")
public class TimeTableCtl extends BaseReportCtl<TimeTableForm, TimeTableDTO, TimeTableServiceInt> {

	@Autowired
	private CourseServiceInt courseService;

	@Autowired
	private SubjectServiceInt subjectService;

	@GetMapping("/preload")
	public ORSResponse preload() {

		List<Map<String, Object>> courseList = courseService.preloadList(new CourseDTO(), userContext);

		Map<String, Object> preload = new HashMap<String, Object>();

		preload.put("courseList", courseList);

		List<Map<String, Object>> subjectList = subjectService.preloadList(new SubjectDTO(), userContext);

		preload.put("subjectList", subjectList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);

		return res;
	}

	/**
	 * Find TimeTable by Course, Subject and Semester
	 *
	 * @param courseId
	 * @param subjectId
	 * @param semester
	 * @return
	 */
	@GetMapping("course/{courseId}/subject/{subjectId}/semester/{semester}")
	public ORSResponse get(@PathVariable Long courseId, @PathVariable Long subjectId, @PathVariable String semester) {

		ORSResponse res = new ORSResponse(true);

		TimeTableDTO dto = baseService.findByCourseSubjectSemester(courseId, subjectId, semester, userContext);

		if (dto != null) {
			res.addData(dto);
		} else {
			res.setSuccess(false);
			res.addMessage("Record not found");
		}

		return res;
	}
}