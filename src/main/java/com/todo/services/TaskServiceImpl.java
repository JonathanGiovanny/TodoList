package com.todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.model.Task;
import com.todo.repositories.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepo;

	@Override
	public Task get(Long id) {
		return taskRepo.getOne(id);
	}

	@Override
	public void save(Task task) {
		taskRepo.save(task);
	}

	@Override
	public void delete(Long id) {
		taskRepo.delete(get(id));
	}
}
