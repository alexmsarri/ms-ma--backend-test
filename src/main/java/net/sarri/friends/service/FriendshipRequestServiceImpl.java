package net.sarri.friends.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sarri.friends.domain.business.UserData;
import net.sarri.friends.domain.jpa.Friendship;
import net.sarri.friends.domain.jpa.FriendshipRequest;
import net.sarri.friends.domain.jpa.User;
import net.sarri.friends.exception.FriendRequestAlreadyExistsException;
import net.sarri.friends.exception.FriendshipRequestNotFoundException;
import net.sarri.friends.exception.UserNotFoundException;
import net.sarri.friends.exception.YouAreAlreadyFriendsException;
import net.sarri.friends.repository.FriendshipRepository;
import net.sarri.friends.repository.FriendshipRequestRepository;
import net.sarri.friends.repository.UserRepository;
import net.sarri.friends.util.FriendsPreconditions;

@Service
public class FriendshipRequestServiceImpl implements FriendshipRequestService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private FriendshipRequestRepository friendshipRequestRepository;

	@Override
	@Transactional(readOnly = true)
	public List<FriendshipRequest> findAllByUsername(String username) {
		return friendshipRequestRepository.findByUserInitiatorUsernameOrUserReceiverUsername(username, username);
	}

	@Override
	@Transactional(readOnly = false)
	public FriendshipRequest create(String username) {
		FriendsPreconditions.checkItsNotMe(username);
		User receiver = userRepository.findById(username).orElseThrow(UserNotFoundException::new);
		if (!receiver.getVisible())
			throw new UserNotFoundException();
		if (userService.me().getFriends().contains(username))
			throw new YouAreAlreadyFriendsException();

		User me = userRepository.findByUsername(userService.me().getUsername());

		if (me.getFriendshipInitiatorRequests().stream()
				.anyMatch(request -> request.getUserReceiver().getUsername().equals(username))
				|| me.getFriendshipReceiverRequests().stream()
						.anyMatch(request -> request.getUserInitiator().getUsername().equals(username)))
			throw new FriendRequestAlreadyExistsException();

		return friendshipRequestRepository.saveAndFlush(new FriendshipRequest(me, receiver));
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String username) {
		FriendsPreconditions.checkItsNotMe(username);
		UserData me = userService.me();
		// Is initiator
		Optional<FriendshipRequest> request = friendshipRequestRepository
				.findByUserInitiatorUsernameAndUserReceiverUsername(me.getUsername(), username);
		if (request.isPresent()) {
			friendshipRequestRepository.delete(request.get());
			return;
		}
		// Is receiver
		request = friendshipRequestRepository.findByUserInitiatorUsernameAndUserReceiverUsername(username,
				me.getUsername());
		if (request.isPresent()) {
			User initiator = request.get().getUserInitiator();
			initiator.setDeclinesCount(initiator.getDeclinesCount() + 1);
			userRepository.saveAndFlush(initiator);
			userService.checkForNewBadges(initiator.getUsername());
			friendshipRequestRepository.delete(request.get());
			return;
		}
		throw new UserNotFoundException();
	}

	@Override
	@Transactional(readOnly = false)
	public void accept(String username) {
		FriendsPreconditions.checkItsNotMe(username);
		UserData me = userService.me();
		FriendshipRequest request = friendshipRequestRepository
				.findByUserInitiatorUsernameAndUserReceiverUsername(username, me.getUsername())
				.orElseThrow(FriendshipRequestNotFoundException::new);
		Friendship friends = new Friendship();
		friends.setDate(LocalDateTime.now());
		friends.getUsers().add(request.getUserInitiator());
		friends.getUsers().add(request.getUserReceiver());
		request.getUserInitiator().getFriendshipInitiatorRequests().remove(request);
		request.getUserReceiver().getFriendshipReceiverRequests().remove(request);
		request.getUserInitiator().getFriendships().add(friends);
		request.getUserReceiver().getFriendships().add(friends);
		friendshipRepository.saveAndFlush(friends);
	}

}
