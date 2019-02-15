package com.todo.services.security;

import com.todo.model.security.User;

public interface UserService {

	/**
	 * Retrieve the user based on the user name
	 * @param user
	 * @return
	 */
	public User getUser(String user);

	/**
	 * Register a user based on the data
	 * @param user
	 */
	public void registerUser(User user);
}
