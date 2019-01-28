package com.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
