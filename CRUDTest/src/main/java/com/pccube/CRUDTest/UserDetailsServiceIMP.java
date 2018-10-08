package com.pccube.CRUDTest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pccube.CRUDTest.entities.User;
import com.pccube.CRUDTest.entities.UserRepository;

@Service
public class UserDetailsServiceIMP implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("The user " + username + " does not exist");
		}
		return new AuthenticatedUser(user);
	}

}
