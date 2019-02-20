package com.todo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.todo.model.Task;
import com.todo.repositories.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepo;

	@Override
	public Task get(Long id, String user) {
		return taskRepo.findByIdAndCreatedBy(id, user);
	}

	@Override
	public List<Task> getAll(Pageable pageable, String user) {
		return taskRepo.findByCreatedBy(pageable, user).get().collect(Collectors.toList());
	}

	@Override
	public void save(Task task) {
		taskRepo.save(task);
	}

	@Override
	public void update(Task task, String user) {
		taskRepo.setTaskFor(task.getTask(), user);
	}

	@Override
	public void delete(Long id, String user) {
		taskRepo.delete(get(id, user));
	}
}
