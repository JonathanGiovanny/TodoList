package com.todo.services.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.todo.model.security.Privilege;
import com.todo.model.security.Role;
import com.todo.repositories.security.UserRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		com.todo.model.security.User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		UserBuilder builder = User.withUsername(username);
		builder.password(user.getPassword());
		builder.authorities(getGrantedAuthorities(getPrivilegesForRoles(user.getRoles())));

		return builder.build();
	}

	/**
	 * Get the set of all the privileges for all the roles that user has
	 * @param roles
	 * @return
	 */
	private Set<Privilege> getPrivilegesForRoles(Set<Role> roles) {
		Set<Privilege> privileges = new HashSet<>();
		roles.forEach(r -> privileges.addAll(r.getPrivileges()));
		return privileges;
	}

	/**
	 * Create the authorities for the privileges list
	 * @param privileges
	 * @return
	 */
	private Set<GrantedAuthority> getGrantedAuthorities(Set<Privilege> privileges) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		privileges.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
		return authorities;
	}
}