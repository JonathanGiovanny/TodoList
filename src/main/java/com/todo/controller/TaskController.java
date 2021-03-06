package com.todo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.model.Task;
import com.todo.services.TaskService;

@RestController
@RequestMapping("/todo")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@PreAuthorize("hasPermission(#id, 'READ')")
	@GetMapping(value = "/tasks/{id}")
	public Task getTask(@PathVariable Long id, Principal principal) {
		return taskService.get(id, principal.getName());
	}

	@PreAuthorize("hasPermission(#this, 'READ')")
	@GetMapping("/tasks")
	public List<Task> getAll(Pageable pageable, Principal principal) {
		for (int i = 0; i < 50; i++) {
			Task t = new Task();
			t.setTask(Integer.toHexString((i + 16) % 256));
			taskService.save(t);
		}
		return taskService.getAll(pageable, principal.getName());
	}

	@PreAuthorize("hasPermission(#task, 'WRITE')")
	@PostMapping("/tasks")
	public void saveTask(@RequestBody Task task) {
		taskService.save(task);
	}

	@PreAuthorize("hasPermission(#task, 'WRITE')")
	@PutMapping("/tasks/{id}")
	public void updateTask(@PathVariable Long id, @RequestBody Task task, Principal principal) {
		task.setId(id);
		taskService.update(task, principal.getName());
	}

	@PreAuthorize("hasPermission(#task, 'DELETE')")
	@DeleteMapping("/tasks/{id}")
	public void delete(@PathVariable Long id, Principal principal) {
		taskService.delete(id, principal.getName());
	}
}
