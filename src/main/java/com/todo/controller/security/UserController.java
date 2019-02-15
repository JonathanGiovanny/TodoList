package com.todo.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.model.security.User;
import com.todo.services.security.UserService;

@RestController
@RequestMapping("/register")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/user")
	public void registerUser(@RequestBody User user) {
		this.userService.registerUser(user);
	}
}
