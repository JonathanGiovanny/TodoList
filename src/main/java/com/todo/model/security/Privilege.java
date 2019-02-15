package com.todo.model.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PRIVILEGES")
public class Privilege implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6644250058874945507L;

	@Id
	@Column
	@SequenceGenerator(name = "PRIVILEGE_SEQ", sequenceName = "PRIVILEGE_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "PRIVILEGE_SEQ")
	private Long id;

	@Column
	private String name;
}
