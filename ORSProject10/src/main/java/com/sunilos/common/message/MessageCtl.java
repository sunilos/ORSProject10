package com.sunilos.common.message;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.ORSResponse;

@RestController
@RequestMapping(value = "Message")
public class MessageCtl extends BaseCtl<MessageForm, MessageDTO, MessageServiceInt> {

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		
		HashMap<String, String> yesNo = new HashMap<String, String>();
		yesNo.put("Yes", "Y");
		yesNo.put("No", "N");
		
		res.addResult("yesNo", yesNo);
		
		HashMap<String, String> types = new HashMap<String, String>();
		types.put("Email", "EMAIL");
		types.put("SMS", "SMS");
		
		res.addResult("types", types);
		
		return res;
	}

	@GetMapping("code/{code}")
	public ORSResponse getCode(@PathVariable String code) {
		ORSResponse res = new ORSResponse(true);
		MessageDTO dto = baseService.findByCode(code, userContext);
		System.out.println("Role " + dto);
		if (dto != null) {
			res.addData(dto);
		} else {
			res.setSuccess(false);
			res.addMessage("Record not found");
		}
		return res;
	}

	@GetMapping("title/{title}")
	public ORSResponse getTitle(@PathVariable String title) {
		ORSResponse res = new ORSResponse(true);
		MessageDTO dto = baseService.findByTitle(title, userContext);
		System.out.println("Role " + dto);
		if (dto != null) {
			res.addData(dto);
		} else {
			res.setSuccess(false);
			res.addMessage("Record not found");
		}
		return res;
	}

}
