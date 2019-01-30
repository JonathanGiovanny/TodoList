package com.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todo.model.Task;
import com.todo.services.TaskService;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;

	@GetMapping("/get")
	public Task get(@RequestBody Long id) {
		return taskService.get(id);
	}

	@GetMapping("/getAll")
	public List<Task> getAll() {
		return taskService.getAll();
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
