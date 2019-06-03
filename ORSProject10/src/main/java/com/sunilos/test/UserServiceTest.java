package com.sunilos.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sunilos.dto.UserDTO;
import com.sunilos.service.UserServiceInt;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceTest {

	@Autowired
	private UserServiceInt service;

	@Test
	public void testGet(){
		//UserDTO dto = service.findById(1);
		//System.out.println(dto);
	}

}
