package com.todo.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.model.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByName(String name);
}
