package com.todo.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.model.security.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

	public Privilege findByName(String name);
}
