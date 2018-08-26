package net.sarri.friends.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sarri.friends.service.UserService;

/**
 * Friendship Entity API
 * 
 * @author alexmsarri
 *
 */
@RestController
@RequestMapping(path = "/friends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@Api("Friends API")
public class FriendshipController {

	@Autowired
	private UserService userService;

	@GetMapping
	@ApiOperation(value = "List Friends of logged User")
	public ResponseEntity<Collection<String>> list() {
		log.debug("List friends for logged user");
		Set<String> friends = userService.me().getFriends();
		if (CollectionUtils.isEmpty(friends))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return ResponseEntity.ok(friends);
	}

}
