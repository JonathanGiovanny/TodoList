package com.todo.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.todo.model.Task;

public interface TaskService {

	/**
	 * Get Task by the info on the Task filter object
	 * @param id
	 * @param user
	 */
	public Task get(Long id, String user);

	/**
	 * Get all the Tasks
	 * @param id
	 * @param user
	 */
	public List<Task> getAll(Pageable pageable, String user);

	/**
	 * Save Task 
	 * @param task
	 */
	public void save(Task task);
	
	/**
	 * Update Task 
	 * @param task
	 * @param user
	 */
	public void update(Task task, String user);

	/**
	 * Delete Task by Id
	 * @param id
	 * @param String user
	 */
	public void delete(Long id, String user);

}
