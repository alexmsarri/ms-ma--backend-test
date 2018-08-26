package net.sarri.friends.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sarri.friends.domain.jpa.FriendshipRequest;
import net.sarri.friends.domain.jpa.FriendshipRequestId;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, FriendshipRequestId> {

	List<FriendshipRequest> findByUserInitiatorUsername(String username);

	List<FriendshipRequest> findByUserInitiatorUsernameOrUserReceiverUsername(String initiator, String receiver);

	Optional<FriendshipRequest> findByUserInitiatorUsernameAndUserReceiverUsername(String username, String username2);

}
