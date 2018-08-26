package net.sarri.friends;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import net.sarri.friends.domain.enums.BadgeEnum;
import net.sarri.friends.domain.enums.FriendshipRequestStatusEnum;
import net.sarri.friends.domain.rest.request.FriendshipRequestRequest;
import net.sarri.friends.domain.rest.request.UserRegisterRequest;
import net.sarri.friends.domain.rest.request.UserUpdateRequest;
import net.sarri.friends.domain.rest.response.FriendshipRequestResponse;
import net.sarri.friends.domain.rest.response.UserResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class FriendsIT {

	@LocalServerPort
	private int port;

	HttpHeaders headers = new HttpHeaders();
	TestRestTemplate restTemplate = new TestRestTemplate();

	private static String DEFAULT_PASSWORD = "password";

	private static String USERS[];

	private String getUriContext(String uri) {
		return "http://localhost:" + port + uri;
	}

	@Before
	public void init() {
		log.debug("Initializing Integration Test");
		// Allow PATCH method calls
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(1100);
		requestFactory.setReadTimeout(1100);
		restTemplate.getRestTemplate().setRequestFactory(requestFactory);

		// Initialize users
		USERS = new String[10];
		IntStream.range(0, 10).forEach(i -> USERS[i] = "testuser" + i);
	}

	/**
	 * Register 10 users
	 */
	@Test
	public void test101Register10Users() {
		for (String username : USERS) {
			UserRegisterRequest userRequest = new UserRegisterRequest(username, DEFAULT_PASSWORD);
			HttpEntity<UserRegisterRequest> entity = new HttpEntity<UserRegisterRequest>(userRequest, headers);
			ResponseEntity<UserResponse> response = restTemplate.exchange(getUriContext("/users"), HttpMethod.POST,
					entity, UserResponse.class);

			UserResponse body = response.getBody();
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals(userRequest.getUsername(), body.getUsername());
			assertEquals(true, body.getVisible());
			assertNotNull(body.getDateJoined());
			assertNull(body.getBadges());
		}
	}

	/**
	 * Create user with short username
	 */
	@Test
	public void test102ErrorCreateUserWithIncorrectUsernameShort() {
		UserRegisterRequest user = new UserRegisterRequest("user", DEFAULT_PASSWORD);
		HttpEntity<UserRegisterRequest> entity = new HttpEntity<UserRegisterRequest>(user, headers);
		ResponseEntity<UserResponse> response = restTemplate.exchange(getUriContext("/users"), HttpMethod.POST, entity,
				UserResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * Create user with large username
	 */
	@Test
	public void test103ErrorCreateUserWithIncorrectUsernameLarge() {
		UserRegisterRequest user = new UserRegisterRequest("largeusername11", DEFAULT_PASSWORD);
		HttpEntity<UserRegisterRequest> entity = new HttpEntity<UserRegisterRequest>(user, headers);
		ResponseEntity<UserResponse> response = restTemplate.exchange(getUriContext("/users"), HttpMethod.POST, entity,
				UserResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * Create user with special characters username
	 */
	@Test
	public void test104ErrorCreateUserWithIncorrectUsernameWithSymbols() {
		UserRegisterRequest user = new UserRegisterRequest("user_", DEFAULT_PASSWORD);
		HttpEntity<UserRegisterRequest> entity = new HttpEntity<UserRegisterRequest>(user, headers);
		ResponseEntity<UserResponse> response = restTemplate.exchange(getUriContext("/users"), HttpMethod.POST, entity,
				UserResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * Create a repeated user
	 */
	@Test
	public void test105ErrorCreateRepeatedUser() {
		UserRegisterRequest user = new UserRegisterRequest(USERS[1], DEFAULT_PASSWORD);
		HttpEntity<UserRegisterRequest> entity = new HttpEntity<UserRegisterRequest>(user, headers);
		ResponseEntity<UserResponse> response = restTemplate.exchange(getUriContext("/users"), HttpMethod.POST, entity,
				UserResponse.class);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	/**
	 * Change visibility of my profile
	 */
	@Test
	public void test106ChangeVisibility() {
		UserUpdateRequest user = new UserUpdateRequest(false, DEFAULT_PASSWORD);
		HttpEntity<UserUpdateRequest> entity = new HttpEntity<UserUpdateRequest>(user, headers);
		ResponseEntity<UserResponse> response = restTemplate.withBasicAuth(USERS[2], DEFAULT_PASSWORD)
				.exchange(getUriContext("/users/testuser2"), HttpMethod.PATCH, entity, UserResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().getVisible());
		assertEquals(USERS[2], response.getBody().getUsername());
	}

	/**
	 * Update my profile
	 */
	@Test
	public void test107UpdateUser() {
		UserUpdateRequest user = new UserUpdateRequest(false, DEFAULT_PASSWORD + 1);
		HttpEntity<UserUpdateRequest> entity = new HttpEntity<UserUpdateRequest>(user, headers);
		ResponseEntity<UserResponse> response = restTemplate.withBasicAuth(USERS[3], DEFAULT_PASSWORD)
				.exchange(getUriContext("/users/" + USERS[3]), HttpMethod.PUT, entity, UserResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().getVisible());
		assertEquals(USERS[3], response.getBody().getUsername());
	}

	/**
	 * List of all the registered users (non-visible users shouldn't appear)
	 */
	@Test
	public void test108ListUsers() {
		ResponseEntity<List<UserResponse>> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD).exchange(
				getUriContext("/users"), HttpMethod.GET, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<List<UserResponse>>() {
				});

		List<UserResponse> users = response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(8, users.size());
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[0])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[1])));
		assertEquals(false, users.stream().anyMatch(u -> u.getUsername().equals(USERS[2])));
		assertEquals(false, users.stream().anyMatch(u -> u.getUsername().equals(USERS[3])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[4])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[5])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[6])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[7])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[8])));
		assertEquals(true, users.stream().anyMatch(u -> u.getUsername().equals(USERS[9])));

	}

	/**
	 * Get my user profile (visible attribute should be returned)
	 */
	@Test
	public void test109GetMyUserProfile() {
		ResponseEntity<UserResponse> response = restTemplate.withBasicAuth(USERS[0], DEFAULT_PASSWORD).exchange(
				getUriContext("/users/" + USERS[0]), HttpMethod.GET, new HttpEntity<Void>(headers), UserResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(USERS[0], response.getBody().getUsername());
		assertNotNull(response.getBody().getVisible());
	}

	/**
	 * Get others profile (visible attribute shouldn't be returned)
	 */
	@Test
	public void test110GetVisibleUserProfile() {
		ResponseEntity<UserResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD).exchange(
				getUriContext("/users/" + USERS[0]), HttpMethod.GET, new HttpEntity<Void>(headers), UserResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(USERS[0], response.getBody().getUsername());
		assertNull(response.getBody().getVisible());
	}

	/**
	 * Get profile of non-existing user
	 */
	@Test
	public void test111GetNonExistingUserProfile() {
		ResponseEntity<UserResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD).exchange(
				getUriContext("/users/testuser11"), HttpMethod.GET, new HttpEntity<Void>(headers), UserResponse.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * Send friend request to a visible user
	 */
	@Test
	public void test201SendFriendshipRequest() {
		FriendshipRequestRequest request = new FriendshipRequestRequest(USERS[4]);
		HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request, headers);
		ResponseEntity<FriendshipRequestResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD)
				.exchange(getUriContext("/friend-requests"), HttpMethod.POST, entity, FriendshipRequestResponse.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(USERS[4], response.getBody().getUsername());
	}

	/**
	 * Send again a friend request that already exists from same side (initiator)
	 */
	@Test
	public void test202SendFriendshipRequestAgainInitiator() {
		FriendshipRequestRequest request = new FriendshipRequestRequest(USERS[4]);
		HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request, headers);
		ResponseEntity<FriendshipRequestResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD)
				.exchange(getUriContext("/friend-requests"), HttpMethod.POST, entity, FriendshipRequestResponse.class);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	/**
	 * Send again a friend request that already exists from other side (receiver)
	 */
	@Test
	public void test203SendFriendshipRequestAgainReceiver() {
		FriendshipRequestRequest request = new FriendshipRequestRequest(USERS[1]);
		HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request, headers);
		ResponseEntity<FriendshipRequestResponse> response = restTemplate.withBasicAuth(USERS[4], DEFAULT_PASSWORD)
				.exchange(getUriContext("/friend-requests"), HttpMethod.POST, entity, FriendshipRequestResponse.class);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	/**
	 * Initiator user has to see the request as initiator status
	 */
	@Test
	public void test204InitiatorHasFriendRequest() {
		ResponseEntity<List<FriendshipRequestResponse>> response = restTemplate
				.withBasicAuth(USERS[1], DEFAULT_PASSWORD).exchange(getUriContext("/friend-requests"), HttpMethod.GET,
						new HttpEntity<Void>(headers),
						new ParameterizedTypeReference<List<FriendshipRequestResponse>>() {
						});

		List<FriendshipRequestResponse> list = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, list.size());
		assertEquals(USERS[4], list.get(0).getUsername());
		assertEquals(FriendshipRequestStatusEnum.INITIATOR.name(), list.get(0).getStatus());
	}

	/**
	 * Receiver user has to see the request as receiver status
	 */
	@Test
	public void test205ReceiverHasFriendRequest() {
		ResponseEntity<List<FriendshipRequestResponse>> response = restTemplate
				.withBasicAuth(USERS[4], DEFAULT_PASSWORD).exchange(getUriContext("/friend-requests"), HttpMethod.GET,
						new HttpEntity<Void>(headers),
						new ParameterizedTypeReference<List<FriendshipRequestResponse>>() {
						});

		List<FriendshipRequestResponse> list = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, list.size());
		assertEquals(USERS[1], list.get(0).getUsername());
		assertEquals(FriendshipRequestStatusEnum.RECEIVER.name(), list.get(0).getStatus());
	}

	/**
	 * User can't send request to himself
	 */
	@Test
	public void test206UserSendsRequestToHimself() {
		FriendshipRequestRequest request = new FriendshipRequestRequest(USERS[1]);
		HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request, headers);
		ResponseEntity<FriendshipRequestResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD)
				.exchange(getUriContext("/friend-requests"), HttpMethod.POST, entity, FriendshipRequestResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * User can't send request to non-existing user
	 */
	@Test
	public void test207UserSendsRequestToNonExistingUser() {
		FriendshipRequestRequest request = new FriendshipRequestRequest("randomuser");
		HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request, headers);
		ResponseEntity<FriendshipRequestResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD)
				.exchange(getUriContext("/friend-requests"), HttpMethod.POST, entity, FriendshipRequestResponse.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * Initiator user tries to accept request
	 */
	@Test
	public void test208InitiatorTriesToAcceptRequest() {
		ResponseEntity<Void> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD).exchange(
				getUriContext("/friend-requests/" + USERS[4]), HttpMethod.POST, new HttpEntity<Void>(headers),
				Void.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * Receiver accepts request
	 */
	@Test
	public void test209ReceiverAcceptsRequest() {
		ResponseEntity<Void> response = restTemplate.withBasicAuth(USERS[4], DEFAULT_PASSWORD).exchange(
				getUriContext("/friend-requests/" + USERS[1]), HttpMethod.POST, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<Void>() {
				});

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	/**
	 * Send again a friend request to a user you are friend of
	 */
	@Test
	public void test210SendAgainFriendRequestAfterBeFriends() {
		FriendshipRequestRequest request = new FriendshipRequestRequest(USERS[4]);
		HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request, headers);
		ResponseEntity<FriendshipRequestResponse> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD)
				.exchange(getUriContext("/friend-requests"), HttpMethod.POST, entity, FriendshipRequestResponse.class);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	/**
	 * Initiator user is friend of receiver
	 */
	@Test
	public void test211BothUsersAreFriendsInitiator() {
		ResponseEntity<List<String>> response = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD).exchange(
				getUriContext("/friends"), HttpMethod.GET, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<List<String>>() {
				});

		List<String> list = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, list.size());
		assertEquals(USERS[4], list.get(0));
	}

	/**
	 * Receiver user is friend of initiator
	 */
	@Test
	public void test212BothUsersAreFriendsReceiver() {
		ResponseEntity<List<String>> response = restTemplate.withBasicAuth(USERS[4], DEFAULT_PASSWORD).exchange(
				getUriContext("/friends"), HttpMethod.GET, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<List<String>>() {
				});

		List<String> list = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, list.size());
		assertEquals(USERS[1], list.get(0));
	}

	/**
	 * User sends multiple friendship requests
	 */
	@Test
	public void test213UserSendsFriendshipRequestToAllUsers() {
		String initiator = USERS[5];

		ResponseEntity<List<UserResponse>> responseList = restTemplate.withBasicAuth(USERS[1], DEFAULT_PASSWORD)
				.exchange(getUriContext("/users"), HttpMethod.GET, new HttpEntity<Void>(headers),
						new ParameterizedTypeReference<List<UserResponse>>() {
						});

		List<UserResponse> users = responseList.getBody();
		assertEquals(HttpStatus.OK, responseList.getStatusCode());

		for (UserResponse user : users) {
			if (!user.getUsername().equals(initiator)) {
				FriendshipRequestRequest request = new FriendshipRequestRequest(user.getUsername());
				HttpEntity<FriendshipRequestRequest> entity = new HttpEntity<FriendshipRequestRequest>(request,
						headers);
				ResponseEntity<FriendshipRequestResponse> response = restTemplate
						.withBasicAuth(initiator, DEFAULT_PASSWORD).exchange(getUriContext("/friend-requests"),
								HttpMethod.POST, entity, FriendshipRequestResponse.class);

				assertEquals(HttpStatus.CREATED, response.getStatusCode());
				assertEquals(user.getUsername(), response.getBody().getUsername());
			}

		}

		ResponseEntity<List<FriendshipRequestResponse>> response = restTemplate
				.withBasicAuth(initiator, DEFAULT_PASSWORD).exchange(getUriContext("/friend-requests"), HttpMethod.GET,
						new HttpEntity<Void>(headers),
						new ParameterizedTypeReference<List<FriendshipRequestResponse>>() {
						});

		List<FriendshipRequestResponse> list = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(7, list.size());

	}

	/**
	 * Initiator user removes a Request
	 */
	@Test
	public void test214InitiatorUserRemovesRequest() {
		ResponseEntity<Void> response = restTemplate.withBasicAuth(USERS[5], DEFAULT_PASSWORD).exchange(
				getUriContext("/friend-requests/" + USERS[1]), HttpMethod.DELETE, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<Void>() {
				});

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	/**
	 * User gets Forever Alone Badge after 3 declines
	 */
	@Test
	public void test215UserReceives3DeclinesAndGetsForeverAloneBadge() {
		ResponseEntity<Void> response1 = restTemplate.withBasicAuth(USERS[9], DEFAULT_PASSWORD).exchange(
				getUriContext("/friend-requests/" + USERS[5]), HttpMethod.DELETE, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<Void>() {
				});

		assertEquals(HttpStatus.NO_CONTENT, response1.getStatusCode());

		ResponseEntity<Void> response2 = restTemplate.withBasicAuth(USERS[8], DEFAULT_PASSWORD).exchange(
				getUriContext("/friend-requests/" + USERS[5]), HttpMethod.DELETE, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<Void>() {
				});

		assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());

		ResponseEntity<Void> response3 = restTemplate.withBasicAuth(USERS[7], DEFAULT_PASSWORD).exchange(
				getUriContext("/friend-requests/" + USERS[5]), HttpMethod.DELETE, new HttpEntity<Void>(headers),
				new ParameterizedTypeReference<Void>() {
				});

		assertEquals(HttpStatus.NO_CONTENT, response3.getStatusCode());
	}

	/**
	 * Check profile of user to see Forever alone Badge
	 */
	@Test
	public void test216UserProfileShowsBadge() {
		ResponseEntity<UserResponse> response = restTemplate.withBasicAuth(USERS[5], DEFAULT_PASSWORD).exchange(
				getUriContext("/users/" + USERS[5]), HttpMethod.GET, new HttpEntity<Void>(headers), UserResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(USERS[5], response.getBody().getUsername());
		assertNotNull(response.getBody().getVisible());
		assertEquals(1, response.getBody().getBadges().size());
		assertEquals(BadgeEnum.FOREVER_ALONE.getName(), response.getBody().getBadges().get(0).getName());
	}

}
