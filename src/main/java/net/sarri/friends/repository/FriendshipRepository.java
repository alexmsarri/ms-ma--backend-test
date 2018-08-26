package net.sarri.friends.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sarri.friends.domain.business.FriendshipData;
import net.sarri.friends.domain.jpa.Friendship;
import net.sarri.friends.domain.jpa.User;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	List<FriendshipData> findByUsersIn(User user);

}
