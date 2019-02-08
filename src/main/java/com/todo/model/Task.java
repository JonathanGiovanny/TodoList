package com.todo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.todo.model.audit.Auditable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false) // Do not add the super fields to the hash and equals
@Entity
@Table(name = "TASKS")
@ToString (callSuper=true)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" }) // Ignoring Hibernate Garbage
public class Task extends Auditable implements Serializable {

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
}
