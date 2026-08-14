package com.sunilos.ctl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.dto.CompanyDTO;
import com.sunilos.form.CompanyForm;
import com.sunilos.service.CompanyServiceInt;

@RestController
@RequestMapping(value = "company")
public class CompanyCtl extends BaseCtl<CompanyForm, CompanyDTO, CompanyServiceInt> {

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		return res;
	}
}
