package com.sunilos.common;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunilos.exception.DatabaseException;
import com.sunilos.exception.DuplicateRecordException;

public abstract class BaseServiceImpl<T extends BaseDTO, D extends BaseDAOInt<T>> implements BaseServiceInt<T> {

	private static Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Autowired
	protected D baseDao;

	@Transactional(readOnly = true)
	public T findById(long id, UserContext userContext) {
		T dto = baseDao.findByPK(id, userContext);
		// T dto baseDao.findByPK(Class<T>, pk)
		return dto;
	}

	@Transactional(readOnly = true)
	public List<T> search(T dto, int pageNo, int pageSize, UserContext userContext) {
		return baseDao.findAll(dto, pageNo, pageSize, userContext);
	}

	@Transactional(readOnly = true)
	public List<T> search(T dto, UserContext userContext) {
		return baseDao.findAll(dto, userContext);
	}

	/**
	 * returns list of key and value pairs for dropdown
	 * 
	 * @param dto
	 * @param userContext
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> preloadList(T dto, UserContext userContext) {
		List<T> list = baseDao.findAll(dto, userContext);
		List<Map<String, Object>> preloadlist = list.stream()
				.map(ele -> Map.<String, Object>of(
						"key", ele.getKey(),
						"value", ele.getValue()))
				.toList();
		return preloadlist;
	}

	@Transactional(readOnly = false)
	public long add(T dto, UserContext userContext) throws DuplicateRecordException {
		// check duplicate
		long pk = baseDao.add(dto, userContext);
		return pk;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T dto, UserContext userContext) throws DuplicateRecordException {
		baseDao.update(dto, userContext);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public long save(T dto, UserContext userContext) throws DuplicateRecordException {
		System.out.println("I am in basedservice .save");
		Long id = dto.getId();
		try {
			if (id != null && id > 0) {
				update(dto, userContext);
				System.out.println("I am in basedservice .update");
			} else {
				id = add(dto, userContext);
				System.out.println("I am in basedservice .add");
			}
		} catch (DuplicateRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T updateFields(Long id, Map<String, Object> fields, UserContext userContext) {
		return baseDao.updateFields(id, fields, userContext);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T delete(long id, UserContext userContext) {
		log.debug("Role Service delete Start");
		T dto = findById(id, userContext);
		if (dto == null) {
			throw new DatabaseException("Record not found");
		}
		baseDao.delete(dto, userContext);
		log.debug("Role Service delete End");
		return dto;
	}

}
