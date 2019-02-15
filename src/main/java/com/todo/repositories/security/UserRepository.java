package com.todo.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.model.security.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(String user);
}
