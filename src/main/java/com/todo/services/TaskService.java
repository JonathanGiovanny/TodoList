package com.todo.services;

import com.todo.model.Task;

public interface TaskService {

	/**
	 * Get Task by the info on the Task filter object
	 * @param id
	 */
	public Task get(Long id);

	/**
	 * Save Task 
	 * @param task
	 */
	public void save(Task task);

	/**
	 * Delete Task by Id
	 * @param id
	 */
	public void delete(Long id);
}
