package com.todo.model.security;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ROLES")
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5160116070649624698L;

	@Id
	@Column
	@SequenceGenerator(name = "ROLE_SEQ", sequenceName = "ROLE_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "ROLE_SEQ")
	private Long id;

	@Column
	private String name;

	@ManyToMany
	@JoinTable(name = "ROLES_PRIVILEGES",
			joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "id"))
	private Set<Privilege> privileges;
}
