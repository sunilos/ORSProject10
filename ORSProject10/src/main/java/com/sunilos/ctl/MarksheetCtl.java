package com.sunilos.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.MarksheetDTO;
import com.sunilos.dto.StudentDTO;
import com.sunilos.form.MarksheetForm;
import com.sunilos.service.MarksheetServiceInt;
import com.sunilos.service.StudentServiceInt;

@RestController
@RequestMapping(value = "marksheet")
public class MarksheetCtl extends BaseCtl<MarksheetForm, MarksheetDTO, MarksheetServiceInt> {

	@Autowired
	private StudentServiceInt studentService;

	@GetMapping("/preload")
	public ORSResponse preload() {
		List<StudentDTO> list = studentService.search(new StudentDTO(), userContext);
		List<Map<String, Object>> studentList = list.stream()
				.map(college -> Map.<String, Object>of(
						"key", college.getKey(),
						"value", college.getValue()))
				.toList();

		Map<String, Object> preload = new HashMap<String, Object>();
		preload.put("studentList", studentList);

		ORSResponse res = new ORSResponse(true);
		res.addData(preload);
		return res;
	}

	@GetMapping("rollno/{rollNo}")
	public ORSResponse rollNo(@PathVariable String rollNo) {
		ORSResponse res = new ORSResponse(true);
		MarksheetDTO dto = baseService.findByRollNo(rollNo, userContext);
		if (dto != null) {
			res.addData(dto);
		} else {
			res.setSuccess(false);
			res.addMessage("Record not found");
		}
		return res;
	}

	@GetMapping("meritlist")
	public ORSResponse getMeritList() {
		List<MarksheetDTO> list = baseService.getMeritList(userContext);
		ORSResponse res = new ORSResponse(true);
		res.addData(list);
		return res;
	}

}
