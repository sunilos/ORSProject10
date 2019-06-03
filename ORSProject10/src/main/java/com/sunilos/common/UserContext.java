package com.sunilos.common;

import com.sunilos.dto.UserDTO;

/**
 * Contains logged in user information
 * 
 * @author Sunil Sahu
 *
 */
public class UserContext {

	private Long userId = 0L;
	private String loginId = "root";
	private String name = null;
	private Long defaultRoleId = 0L;
	private String roleName = "root";
	private Long orgId = 0L;
	private String orgName = "root";
	private Long orgImageId = 0L;

	private UserDTO userDTO = null;

	public UserContext() {
	}

	public UserContext(UserDTO dto) {

		this.userDTO = dto;
		this.loginId = dto.getLoginId();
		this.name = dto.getName();
		this.defaultRoleId = dto.getRoleId();
		this.orgId = dto.getOrgId();
		this.roleName = dto.getRoleName();
		this.userId = dto.getId();

		System.out.println("____________ user context ------------ " + name);

		/*
		 * if (roles == null && userDTO != null) { roles = new
		 * HashSet<String>(); Iterator<UserRoleDTO> it =
		 * userDTO.getUserRoles().iterator(); while (it.hasNext()) {
		 * roles.add(it.next().getRoleName()); } }
		 */

	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDefaultRoleId() {
		return defaultRoleId;
	}

	public void setDefaultRoleId(Long defaultRoleId) {
		this.defaultRoleId = defaultRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgImageId() {
		return orgImageId;
	}

	public void setOrgImageId(Long orgImageId) {
		this.orgImageId = orgImageId;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
