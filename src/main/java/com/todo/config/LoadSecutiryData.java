package com.todo.config;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.todo.model.security.Privilege;
import com.todo.model.security.Role;
import com.todo.model.security.User;
import com.todo.repositories.security.PrivilegeRepository;
import com.todo.repositories.security.RoleRepository;
import com.todo.repositories.security.UserRepository;

@Component
public class LoadSecutiryData {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PrivilegeRepository privilegeRepo;

	@Autowired
	private PasswordEncoder passEnconder;

	/**
	 * Load base data to be able to have an admin by default
	 */
	@PostConstruct
	public void init() {
		// Create the privileges
		Privilege readPrivilege = new Privilege();
		readPrivilege.setName("READ_PRIVILEGE");
		privilegeRepo.save(readPrivilege);

		Privilege writePrivilege = new Privilege();
		writePrivilege.setName("WRITE_PRIVILEGE");
		privilegeRepo.save(writePrivilege);

		Privilege deletePrivilege = new Privilege();
		deletePrivilege.setName("DELETE_PRIVILEGE");
		privilegeRepo.save(deletePrivilege);

		// Create the roles
		Role adminRole = createRole("ROLE_ADMIN", readPrivilege, writePrivilege, deletePrivilege);
		roleRepo.save(adminRole);

		Role editorRole = createRole("ROLE_EDITOR", readPrivilege, writePrivilege);
		roleRepo.save(editorRole);

		Role userRole = createRole("ROLE_USER", readPrivilege);
		roleRepo.save(userRole);

		// Create the admin user
		User adminUser = new User();
		adminUser.setUsername("admin");
		adminUser.setPassword(passEnconder.encode("admin"));
		adminUser.setRoles(new HashSet<>(Arrays.asList(adminRole)));
		userRepo.save(adminUser);
	}

	/**
	 * Create the role based on the name and the list of given privileges
	 * 
	 * @param name
	 * @param privileges
	 * @return
	 */
	private Role createRole(String name, Privilege... privileges) {
		Role role = new Role();
		role.setName(name);
		role.setPrivileges(new HashSet<>(Arrays.asList(privileges)));
		return role;
	}
}
