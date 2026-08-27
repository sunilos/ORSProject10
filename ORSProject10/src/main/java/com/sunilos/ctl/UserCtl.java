package com.sunilos.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sunilos.common.BaseReportCtl;
import com.sunilos.common.ORSResponse;
import com.sunilos.service.DocumentServiceInt;
import com.sunilos.dto.DocumentDTO;
import com.sunilos.dto.RoleDTO;
import com.sunilos.dto.UserDTO;
import com.sunilos.form.ChangePasswordForm;
import com.sunilos.form.MyProfileForm;
import com.sunilos.form.UserForm;
import com.sunilos.service.RoleServiceInt;
import com.sunilos.service.UserServiceInt;

/**
 * REST API for user account operations - profile, password and photo
 * management. Base path: {@code /user}. Also inherits the generic CRUD
 * endpoints from {@link BaseCtl} (search, get by id, add, update, delete).
 *
 * <p>
 * Endpoints:
 * <ul>
 * <li>{@code GET /user/preload} - {@link #preload()}: dropdown data (role
 * list) for the user form</li>
 * <li>{@code POST /user/myprofile} - {@link #myProfile(MyProfileForm,
 * BindingResult)}: updates the profile of the logged-in user</li>
 * <li>{@code POST /user/changepassword} -
 * {@link #changePassword(ChangePasswordForm, BindingResult)}: changes the
 * password of the logged-in user</li>
 * <li>{@code PUT /user/profilePhoto/{userId}} -
 * {@link #uploadUserProfilePhoto(Long, MultipartFile)}: uploads/replaces the
 * profile photo of the given user id</li>
 * </ul>
 *
 * <p>
 * The forgot-password flow lives at {@code POST /auth/fp/{login}} (see
 * {@link LoginCtl#forgotPassword(String)}), not here.
 */
@RestController
@RequestMapping(value = "user")
public class UserCtl extends BaseReportCtl<UserForm, UserDTO, UserServiceInt> {

	private static final Logger log = LoggerFactory.getLogger(UserCtl.class);

	@Autowired
	RoleServiceInt roleService = null;

	@Autowired
	DocumentServiceInt documentService;

	@Value("${photo.base-path}")
	private String photoBasePath;

	@GetMapping("/preload")
	public ORSResponse preload() {
		ORSResponse res = new ORSResponse(true);
		RoleDTO dto = new RoleDTO();

		List<Map<String, Object>> roleList = roleService.preloadList(dto, userContext);

		Map<String, List<Map<String, Object>>> preload = new HashMap<>();
		preload.put("roleList", roleList);
		res.addData(preload);
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
		// dto.setLoginId(form.getLogin());
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

	@Autowired
	private DocumentCtl documentCtl;

	/**
	 * Uploads profile picture of given user id
	 *
	 * @param userId
	 * @param file
	 * @return
	 */
	@PutMapping("/profilePhoto/{userId}")
	public ORSResponse uploadUserProfilePhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {

		if (file == null || file.isEmpty()) {
			return new ORSResponse(false, "No file provided");
		}

		UserDTO userDTO = baseService.findById(userId, userContext);
		if (userDTO == null) {
			return new ORSResponse(false, "User not found");
		}

		long oldImageId = userDTO.getImageId();

		ORSResponse docResponse = documentCtl.addFile(file, userDTO.getValue() + " user profile photo", userContext);
		if (!docResponse.isSuccess()) {
			return new ORSResponse(false, docResponse.getMessage());
		}

		DocumentDTO uploadedDocumentDTO = docResponse.getData(DocumentDTO.class);
		userDTO.setImageId(uploadedDocumentDTO.getId());
		baseService.save(userDTO, userContext);

		if (oldImageId > 0) {
			try {
				documentCtl.deleteDocument(oldImageId, userContext);
			} catch (Exception e) {
				log.warn("Failed to delete old profile photo (document id={}) for user id={}", oldImageId, userId, e);
			}
		}

		return new ORSResponse(true, "Profile photo uploaded successfully");
	}

}
