package net.sarri.friends.controller;

import static net.sarri.friends.domain.mapper.FriendshipRequestMapperRest.toResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sarri.friends.domain.jpa.FriendshipRequest;
import net.sarri.friends.domain.mapper.FriendshipRequestMapperRest;
import net.sarri.friends.domain.rest.request.FriendshipRequestRequest;
import net.sarri.friends.domain.rest.response.FriendshipRequestResponse;
import net.sarri.friends.service.FriendshipRequestService;
import net.sarri.friends.service.UserService;

/**
 * Friendship Request Entity API
 * 
 * @author alexmsarri
 *
 */
@RestController
@RequestMapping(path = "/friend-requests", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@Api("Friend Request")
public class FriendshipRequestController {

	@Autowired
	private UserService userService;

	@Autowired
	private FriendshipRequestService friendshipRequestService;

	@GetMapping
	@ApiOperation("List all Friend Requests")
	public ResponseEntity<List<FriendshipRequestResponse>> list() {
		log.debug("List friend requests for logged user");
		List<FriendshipRequest> requests = friendshipRequestService.findAllByUsername(userService.me().getUsername());
		if (CollectionUtils.isEmpty(requests))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return ResponseEntity.ok(FriendshipRequestMapperRest.toResponse(requests, userService.me().getUsername()));
	}

	@PostMapping
	@ApiOperation("Send Friend Request to User")
	public ResponseEntity<FriendshipRequestResponse> sendRequest(
			@Valid @RequestBody(required = true) FriendshipRequestRequest request) throws URISyntaxException {
		log.debug("Send Friendship Request to user: {}", request);
		return ResponseEntity.created(new URI("/friend-requests/" + request.getUsername())).body(
				toResponse(friendshipRequestService.create(request.getUsername()), userService.me().getUsername()));
	}

	@PostMapping(path = "/{username}")
	@ApiOperation("Accept Friend Request")
	public ResponseEntity<Void> accept(@PathVariable(required = true) @Valid @NotEmpty String username) {
		log.debug("Accept Friendship Request of {}", username);
		friendshipRequestService.accept(username);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/friends/{username}")
				.buildAndExpand(username).toUri()).build();
	}

	@DeleteMapping(path = "/{username}")
	@ApiOperation("Decline or Delete Friend Request")
	public ResponseEntity<Void> decline(@PathVariable(required = true) @Valid @NotEmpty String username) {
		friendshipRequestService.delete(username);
		return ResponseEntity.noContent().build();
	}

}
