package com.todo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASKS")
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1845948248783870820L;

	@Id
	@Column
	@GeneratedValue
	private Long id;

	@Column
	private String task;

	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate = LocalDateTime.now();

	/**
	 * Default Constructor
	 */
	public Task() {
		super();
	}

	/**
	 * Full arguments constructor
	 * 
	 * @param id
	 * @param task
	 */
	public Task(Long id, String task, LocalDateTime creationDate) {
		super();
		this.id = id;
		this.task = task;
		this.creationDate = creationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 36;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}
}
