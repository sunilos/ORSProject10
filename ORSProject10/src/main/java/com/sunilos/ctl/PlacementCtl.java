package com.sunilos.ctl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.dto.CompanyDTO;
import com.sunilos.dto.PlacementDTO;
import com.sunilos.dto.StudentDTO;
import com.sunilos.form.PlacementForm;
import com.sunilos.service.CollegeServiceInt;
import com.sunilos.service.CompanyServiceInt;
import com.sunilos.service.PlacementServiceInt;
import com.sunilos.service.StudentServiceInt;

@RestController
@RequestMapping(value = "placement")
public class PlacementCtl extends BaseCtl<PlacementForm, PlacementDTO, PlacementServiceInt> {

	@Autowired
	private StudentServiceInt studentService;

	@Autowired
	private CollegeServiceInt collegeService;

	@Autowired
	private CompanyServiceInt companyService;

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		List<StudentDTO> studentList = studentService.search(new StudentDTO(), userContext);
		List<CollegeDTO> collegeList = collegeService.search(new CollegeDTO(), userContext);
		List<CompanyDTO> companyList = companyService.search(new CompanyDTO(), userContext);
		res.addResult("studentList", studentList);
		res.addResult("collegeList", collegeList);
		res.addResult("companyList", companyList);
		return res;
	}
}
