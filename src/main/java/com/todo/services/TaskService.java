package com.todo.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.todo.model.Task;

public interface TaskService {

	/**
	 * Get Task by the info on the Task filter object
	 * @param id
	 */
	public Task get(Long id);

	/**
	 * Get all the Tasks
	 * @param id
	 */
	public List<Task> getAll(Pageable pageable);

	/**
	 * Save Task 
	 * @param task
	 */
	public void save(Task task);
	
	/**
	 * Update Task 
	 * @param task
	 */
	void update(Task task);

	/**
	 * Delete Task by Id
	 * @param id
	 */
	public void delete(Long id);

}
