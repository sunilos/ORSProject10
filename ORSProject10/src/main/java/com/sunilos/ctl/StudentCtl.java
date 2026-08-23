package com.sunilos.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseReportCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.dto.StudentDTO;
import com.sunilos.form.StudentForm;
import com.sunilos.service.CollegeServiceInt;
import com.sunilos.service.StudentServiceInt;

@RestController
@RequestMapping(value = "student")
public class StudentCtl extends BaseReportCtl<StudentForm, StudentDTO, StudentServiceInt> {

	@Autowired
	private CollegeServiceInt collegeService;

	@GetMapping("/preload")
	public ORSResponse preload() {

		List<Map<String, Object>> collegeList = collegeService.preloadList(new CollegeDTO(), userContext);

		Map<String, Object> preload = new HashMap<String, Object>();
		preload.put("collegeList", collegeList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);
		return res;
	}
}
