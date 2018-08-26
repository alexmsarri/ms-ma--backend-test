package net.sarri.friends.controller;

import static net.sarri.friends.domain.mapper.UserMapperRest.fromBusiness;
import static net.sarri.friends.domain.mapper.UserMapperRest.toBusiness;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sarri.friends.domain.business.UserData;
import net.sarri.friends.domain.rest.request.UserChangeVisibilityRequest;
import net.sarri.friends.domain.rest.request.UserRegisterRequest;
import net.sarri.friends.domain.rest.request.UserUpdateRequest;
import net.sarri.friends.domain.rest.response.UserResponse;
import net.sarri.friends.service.UserService;

/**
 * Entity User API
 * 
 * @author alexmsarri
 *
 */
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api("Users")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Get all Users
	 * 
	 * @return User list
	 */
	@GetMapping
	@ApiOperation("List all visible Users")
	public ResponseEntity<List<UserResponse>> list() {
		List<UserData> userList = userService.findByVisibleIsTrue();
		if (CollectionUtils.isEmpty(userList))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return ResponseEntity.ok(fromBusiness(userList));
	}

	/**
	 * Add new user
	 * 
	 * @param New User information
	 * @return The new user
	 */
	@PostMapping
	@ApiOperation("Register new User")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody(required = true) UserRegisterRequest user) {
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
						.buildAndExpand(user.getUsername()).toUri())
				.body(fromBusiness(userService.register(toBusiness(user))));
	}

	/**
	 * Get User Profile
	 * 
	 * @param username Username of User to search
	 * @return The user information
	 */
	@GetMapping(path = "/{username}")
	@ApiOperation("User profile")
	public ResponseEntity<UserResponse> findByUsername(@PathVariable(required = true) String username) {
		UserData user = userService.findByUsernameAndVisibleIsTrue(username);
		if (Objects.nonNull(user))
			return ResponseEntity.ok(fromBusiness(user, username.equals(userService.me().getUsername())));
		else
			return ResponseEntity.notFound().build();
	}

	/**
	 * Set new value for User visibility attribute
	 * 
	 * @param username Username of the user to update
	 * @param user     New information for the user
	 * @return The changed User
	 */
	@PatchMapping(path = "/{username}")
	@ApiOperation("User profile visibility change")
	public ResponseEntity<UserResponse> updateVisibility(@PathVariable String username,
			@Valid @RequestBody UserChangeVisibilityRequest user) {
		return ResponseEntity.ok(fromBusiness(userService.updateUser(username, toBusiness(user))));
	}

	/**
	 * Update a User
	 * 
	 * @param username Username of the User to update
	 * @param user     New information for the user
	 * @return The changed User
	 */
	@PutMapping(path = "/{username}")
	@ApiOperation("User information change")
	public ResponseEntity<UserResponse> updateUser(@PathVariable String username,
			@Valid @RequestBody UserUpdateRequest user) {
		return ResponseEntity.ok(fromBusiness(userService.updateUser(username, toBusiness(user)),
				username.equals(userService.me().getUsername())));
	}

	/**
	 * Friend list of a User
	 * 
	 * @param username Username of the user
	 * @return The friend list
	 */
	@GetMapping(path = "/{username}/friends")
	@ApiOperation("List Friends of an User")
	public ResponseEntity<Collection<String>> friendList(@PathVariable String username) {
		Set<String> friends = userService.findByUsernameAndVisibleIsTrue(username).getFriends();
		if (CollectionUtils.isEmpty(friends))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return ResponseEntity.ok(friends);
	}

}
