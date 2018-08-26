package net.sarri.friends.service;

import java.util.List;

import net.sarri.friends.domain.jpa.FriendshipRequest;

public interface FriendshipRequestService {

	List<FriendshipRequest> findAllByUsername(String username);

	FriendshipRequest create(String request);

	void delete(String username);

	void accept(String username);
}
