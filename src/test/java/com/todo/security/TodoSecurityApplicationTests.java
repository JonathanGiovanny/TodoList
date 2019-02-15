package com.todo.security;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.todo.controller.security.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoSecurityApplicationTests {

	@Autowired
	private UserController controller;

	@Test
	public void contextLoads() {
		assertNotEquals(null, controller);
	}
}
