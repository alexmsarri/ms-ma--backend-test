package net.sarri.friends.service;

import java.util.List;

import net.sarri.friends.domain.business.UserData;

public interface UserService {

	List<UserData> findAll();

	UserData register(UserData user);

	UserData findByUsername(String username);

	UserData updateUser(String username, UserData user);

	List<UserData> findByVisibleIsTrue();

	UserData findByUsernameAndVisibleIsTrue(String username);

	UserData me();

	UserData findByUsernameSafe(String username);

	void checkForNewBadges(String initiator);
}
