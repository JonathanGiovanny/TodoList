package com.todo.services.security;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.model.security.User;
import com.todo.repositories.security.RoleRepository;
import com.todo.repositories.security.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passEnconder;

	@Override
	public User getUser(String user) {
		return userRepo.findByUsername(user);
	}

	@Override
	public void registerUser(User user) {
		if (getUser(user.getUsername()) != null) {
			throw new RuntimeException("The username already exists");
		}

		// All the new users will have the role user
		user.setRoles(new HashSet<>(Arrays.asList(roleRepo.findByName("ROLE_USER"))));
		user.setPassword(passEnconder.encode(user.getPassword()));
		userRepo.save(user);
	}
}
