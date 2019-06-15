package com.sunilos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sunilos.common.BaseDAOImpl;
import com.sunilos.common.UserContext;
import com.sunilos.dto.RoleDTO;
import com.sunilos.dto.UserDTO;

/**
 * Contains User CRUD operations
 * 
 * @author DELL
 *
 */
@Repository
public class UserDAOImpl extends BaseDAOImpl<UserDTO> implements UserDAOInt {

	@Override
	public Class<UserDTO> getDTOClass() {
		return UserDTO.class;
	}

	@Override
	protected List<Predicate> getWhereClause(UserDTO dto, CriteriaBuilder builder, Root<UserDTO> qRoot) {

		// Create where conditions
		List<Predicate> whereCondition = new ArrayList<Predicate>();

		if (!isEmptyString(dto.getFirstName())) {

			whereCondition.add(builder.like(qRoot.get("firstName"), dto.getFirstName() + "%"));
		}

		if (!isEmptyString(dto.getLastName())) {

			whereCondition.add(builder.like(qRoot.get("lastName"), dto.getLastName() + "%"));
		}
		if (!isEmptyString(dto.getLoginId())) {

			whereCondition.add(builder.equal(qRoot.get("loginId"), dto.getLoginId()));
		}
		if (!isEmptyString(dto.getPassword())) {

			whereCondition.add(builder.equal(qRoot.get("password"), dto.getPassword()));
		}

		if (!isEmptyString(dto.getStatus())) {

			whereCondition.add(builder.equal(qRoot.get("status"), dto.getStatus()));
		}

		if (!isZeroNumber(dto.getRoleId())) {

			whereCondition.add(builder.equal(qRoot.get("roleId"), dto.getRoleId()));
		}

		if (!isZeroNumber(dto.getImageId())) {

			whereCondition.add(builder.equal(qRoot.get("imageId"), dto.getImageId()));
		}

		if (!isEmptyString(dto.getEmail())) {

			whereCondition.add(builder.equal(qRoot.get("email"), dto.getEmail()));
		}

		if (isNotNull(dto.getDob())) {

			whereCondition.add(builder.equal(qRoot.get("dob"), dto.getDob()));
		}

		return whereCondition;
	}

	@Autowired
	RoleDAOInt roleDao;

	@Override
	protected void populate(UserDTO dto, UserContext userContext) {
		if (dto.getRoleId() != null && dto.getRoleId() > 0) {
			RoleDTO roleDto = roleDao.findByPK(dto.getRoleId(), userContext);
			dto.setRoleName(roleDto.getName());
		}
	}

}
