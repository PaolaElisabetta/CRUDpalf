package com.pccube.CRUDTest;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pccube.CRUDTest.entities.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AuthenticatedUser implements UserDetails {

	private User user;

	public AuthenticatedUser(User user) {
		this.user = user;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getType()));

		return authorities;

	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}
}