package com.sunilos.ctl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CollegeDTO;
import com.sunilos.form.CollegeForm;
import com.sunilos.service.CollegeServiceInt;

@RestController
@RequestMapping(value = "College")
public class CollegeCtl extends BaseCtl<CollegeForm, CollegeDTO, CollegeServiceInt> {

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		return res;
	}
}
