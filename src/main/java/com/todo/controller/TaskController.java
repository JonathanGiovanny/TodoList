package com.todo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.model.Task;
import com.todo.services.TaskService;

@RestController
public class TaskController {

	private TaskService taskService;

	@GetMapping("/get")
	public Task get(Long id) {
		return taskService.get(id);
	}

	@PostMapping("/save")
	public void save(Task task) {
		taskService.save(task);
	}

	@DeleteMapping("/delete")
	public void delete(Long id) {
		taskService.delete(id);
	}
}
