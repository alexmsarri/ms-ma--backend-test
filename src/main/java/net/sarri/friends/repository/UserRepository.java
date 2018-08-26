package net.sarri.friends.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sarri.friends.domain.jpa.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	List<User> findByVisibleIsTrue();

	Optional<User> findByUsernameAndVisibleIsTrue(String username);

	User findByUsername(String username);

}
