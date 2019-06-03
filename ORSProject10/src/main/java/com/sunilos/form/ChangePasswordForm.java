package com.sunilos.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.sunilos.common.BaseForm;

/**
 * Contains change password form elements and their declarative input validations.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class ChangePasswordForm extends BaseForm {

	@NotEmpty
	@Size(min = 2, max = 10)
	private String oldPassword;

	@NotEmpty
	@Size(min = 2, max = 10)
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
