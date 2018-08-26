package net.sarri.friends.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.sarri.friends.service.UserDetailsServiceImpl;

/**
 * Basic Security Configuration
 * 
 * @author alexmsarri
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl authDetailsService;

	/**
	 * Global manager for user authentication. It adds an Admin user.
	 * 
	 * @param auth Authentication builder
	 * @throws Exception Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("administrator").password("password").roles("ADMIN");
		auth.userDetailsService(authDetailsService).passwordEncoder(passwordEncoder());
	}

	/**
	 * Security Paths Configuration
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Public resources for Swagger
		http.authorizeRequests().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
				"/swagger-resources/**", "/configuration/security", "/swagger-ui.html**", "/webjars/**").permitAll();
		// Allow the Registration of users to Anonymous and Admin Users
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").hasAnyRole("ANONYMOUS", "ADMIN");
		// All other requests requires to be authenticated
		http.authorizeRequests().anyRequest().authenticated();
		// Enable Basic Http Security and disable CSRF
		http.httpBasic().and().csrf().disable();
	}

	/**
	 * Password encoder
	 * 
	 * @return Encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}