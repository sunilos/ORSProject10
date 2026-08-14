package com.sunilos.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sunilos.service.UserServiceInt;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserServiceInt service;

	@Test
	public void testGet(){
		//UserDTO dto = service.findById(1);
		//System.out.println(dto);
	}

}
