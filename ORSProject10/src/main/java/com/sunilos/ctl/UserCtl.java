package com.sunilos.ctl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sunilos.common.BaseCtl;
import com.sunilos.common.DropdownList;
import com.sunilos.common.ORSResponse;
import com.sunilos.common.attachment.AttachmentDTO;
import com.sunilos.common.attachment.AttachmentServiceInt;
import com.sunilos.common.mail.EmailDTO;
import com.sunilos.common.mail.EmailServiceImpl;
import com.sunilos.dto.RoleDTO;
import com.sunilos.dto.UserDTO;
import com.sunilos.form.ChangePasswordForm;
import com.sunilos.form.ForgetPasswordForm;
import com.sunilos.form.MyProfileForm;
import com.sunilos.form.UserForm;
import com.sunilos.service.RoleServiceInt;
import com.sunilos.service.UserServiceInt;

@RestController
@RequestMapping(value = "User")
public class UserCtl extends BaseCtl<UserForm, UserDTO, UserServiceInt> {

	@Autowired
	RoleServiceInt roleService = null;

	@Autowired
	AttachmentServiceInt attachmentService;

	/**
	 * Send email
	 */
	@Autowired
	public EmailServiceImpl emailSender;

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		RoleDTO dto = new RoleDTO();
		dto.setStatus(RoleDTO.ACTIVE);
		List<DropdownList> list = roleService.search(dto, userContext);
		res.addResult("roleList", list);
		return res;
	}

	/**
	 * Updates profile of logged in user
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("myprofile")
	public ORSResponse myProfile(@RequestBody @Valid MyProfileForm form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (!res.isSuccess()) {
			return res;
		}

		UserDTO dto = baseService.findById(userContext.getUserId(), userContext);
		dto.setFirstName(form.getFirstName());
		dto.setLastName(form.getLastName());
		//dto.setLoginId(form.getLogin());
		dto.setDob(form.getDob());
		dto.setPhone(form.getMobileNo());
		dto.setGender(form.getGender());

		baseService.update(dto, userContext);

		return res;
	}

	/**
	 * Changes password of logged-in user
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("changepassword")
	public ORSResponse changePassword(@RequestBody @Valid ChangePasswordForm form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);

		if (!res.isSuccess()) {
			return res;
		}

		if (userContext == null) {
			res.setSuccess(false);
			res.addMessage("You are not logged-in");
			return res;
		}

		UserDTO dto = userContext.getUserDTO();

		UserDTO changedDto = baseService.changePassword(dto.getLoginId(), form.getOldPassword(), form.getNewPassword(),
				userContext);

		if (changedDto == null) {
			res.setSuccess(false);
			res.addMessage("Invalid old password");
			return res;
		}

		res.setSuccess(true);
		res.addMessage("Password has been changed");

		return res;
	}

	/**
	 * Forgot password
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("forgetPassword")
	public ORSResponse forgetPassword(@RequestBody @Valid ForgetPasswordForm form, BindingResult bindingResult) {

		ORSResponse res = valiate(bindingResult);
		System.out.println("form.getLogin(===="+form.getLogin());

		UserDTO fDTO = baseService.forgotPassword(form.getLogin());
 
		if (fDTO == null) {
			res.setSuccess(false);
			res.addMessage("LoginId / Email not found.");
			return res;
		} else {
			String code = "101";
			EmailDTO dto = new EmailDTO();
			dto.addTo(fDTO.getEmail());
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", "101");
			dto.setMessageCode(code, params);
			emailSender.send(dto, null);
			res.setSuccess(true);
			res.addMessage("Hello " + fDTO.getFirstName() + " " + fDTO.getLastName()
					+ " ! Your password has been sent on your email.");

		}

		return res;
	}

	/**
	 * Uploads user profile picture of logged in user.
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@PostMapping("/profilePic")
	public ORSResponse uploadPic(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		return uploadPic(userContext.getUserId(), file, req);
	}

	/**
	 * Uploads profile picture of given user id
	 * 
	 * @param userId
	 * @param file
	 * @param req
	 * @return
	 */
	@PostMapping("/profilePic/{userId}")
	public ORSResponse uploadPic(@PathVariable Long userId, @RequestParam("file") MultipartFile file,
			HttpServletRequest req) {

		System.out.println("User ID id --------------" + userId);

		UserDTO userDTO = baseService.findById(userId, userContext);

		AttachmentDTO doc = new AttachmentDTO(file);
		doc.setDescription("Profile picture");
		doc.setPath(req.getServletPath());
		doc.setUserId(userId);

		if (userDTO.getImageId() != null && userDTO.getImageId() > 0) {
			doc.setId(userDTO.getImageId());
		}
		Long imageId = attachmentService.save(doc, userContext);

		// Update new image id
		if (userDTO.getImageId() == null || userDTO.getImageId() == 0) {
			userDTO.setImageId(imageId);
			baseService.update(userDTO, userContext);
		}

		ORSResponse res = new ORSResponse();
		res.setSuccess(true);
		res.addResult("imageId", imageId);

		return res;
	}

	/**
	 * Downloads profile picture of logged in user
	 * 
	 * @param response
	 */
	@GetMapping("/profilePic")
	public @ResponseBody void downloadPic(HttpServletResponse response) {
		downloadPic(userContext.getUserId(), response);
	}

	/**
	 * Downloads profile picture of given user id
	 * 
	 * @param userId
	 * @param response
	 */
	@GetMapping("/profilePic/{userId}")
	public @ResponseBody void downloadPic(@PathVariable Long userId, HttpServletResponse response) {

		UserDTO userDTO = baseService.findById(userId, userContext);
		AttachmentDTO attachmentDTO = attachmentService.findById(userDTO.getImageId(), userContext);
		try {
			if (attachmentDTO != null) {
				response.setContentType(attachmentDTO.getType());
				OutputStream out = response.getOutputStream();
				out.write(attachmentDTO.getDoc());
				out.close();
			} else {
				response.getWriter().write("ERROR: File not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Uploads a document for a user
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@PostMapping("/doc/{userId}")
	public ORSResponse upload(@PathVariable Long userId, @RequestParam(required = false) String description,
			@RequestParam("file") MultipartFile file, HttpServletRequest req) {

		AttachmentDTO doc = new AttachmentDTO(file);
		doc.setDescription(description);
		doc.setPath(req.getServletPath());
		doc.setUserId(userId);

		Long pk = attachmentService.save(doc, userContext);

		ORSResponse res = new ORSResponse();
		res.setSuccess(true);
		res.addResult("docId", pk);

		return res;
	}

	/**
	 * Downloads user document
	 * 
	 * @param id
	 * @param response
	 */
	@GetMapping("/doc/{id}")
	public @ResponseBody void download(@PathVariable Long id, HttpServletResponse response) {

		AttachmentDTO attachmentDTO = attachmentService.findById(id, userContext);
		try {
			if (attachmentDTO != null) {
				response.setContentType(attachmentDTO.getType());
				OutputStream out = response.getOutputStream();
				out.write(attachmentDTO.getDoc());
				out.close();
			} else {
				response.getWriter().write("ERROR: File not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Forgot Password 
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@GetMapping("forgotPassword/{loginId}")
	public ORSResponse myProfile(@PathVariable String loginId, HttpServletResponse response) {
		ORSResponse res=new ORSResponse();
		UserDTO userDto = baseService.forgotPassword(loginId);
		try {
			if (userDto != null) {
				res.addData(userDto);
				res.setSuccess(true);
			} else {
				res.setSuccess(false);
				res.addMessage("Login id is not exist");
			}
		} catch (Exception e) {
			res.addMessage(e.getMessage());
		}
		return res;
	}
	
}
