package com.todo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.todo.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	public Task findByIdAndCreatedBy(Long id, String createdBy);
	
	public Page<Task> findByCreatedBy(Pageable pageable, String createdBy);

	@Modifying
	@Transactional
	@Query("UPDATE Task t SET t.task = :task WHERE t.createdBy = :user")
	public void setTaskFor(@Param("task") String task, @Param("user") String user);
}
