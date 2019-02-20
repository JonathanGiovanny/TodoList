package com.todo.services.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
		builder.authorities(getGrantedAuthorities(user.getRoles()));

		return builder.build();
	}

	/**
	 * Create the authorities for the privileges list
	 * 
	 * @param roles
	 * @return
	 */
	private Set<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
		return roles.stream().flatMap((Role r) -> r.getPrivileges().stream()).map(p -> p.getName())
				.distinct().map(p -> new SimpleGrantedAuthority(p)).collect(Collectors.toSet());
	}
}