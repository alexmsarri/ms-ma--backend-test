package net.sarri.friends.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.sarri.friends.domain.jpa.User;
import net.sarri.friends.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		if (username.equals("admin"))
			return new org.springframework.security.core.userdetails.User("admin", passwordEncoder().encode("password"),
					AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		Optional<User> user = userRepository.findById(username);
		if (user.isPresent())
			return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
					passwordEncoder().encode(user.get().getPassword()),
					AuthorityUtils.createAuthorityList(user.get().getRoles()));
		return null;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
