package net.sarri.friends.service;

import static net.sarri.friends.domain.mapper.UserMapperData.fromJpa;
import static net.sarri.friends.domain.mapper.UserMapperData.toJpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sarri.friends.domain.business.UserData;
import net.sarri.friends.domain.enums.BadgeEnum;
import net.sarri.friends.domain.jpa.User;
import net.sarri.friends.exception.UserAlreadyExistsException;
import net.sarri.friends.exception.UserNotFoundException;
import net.sarri.friends.repository.UserRepository;
import net.sarri.friends.util.FriendsPreconditions;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<UserData> findAll() {
		return fromJpa(userRepository.findAll());
	}

	@Override
	@Transactional(readOnly = false)
	public UserData register(UserData user) {
		if (userRepository.existsById(user.getUsername()))
			throw new UserAlreadyExistsException();
		else {
			user.setDateJoined(LocalDateTime.now());
			user.setVisible(true);
			return fromJpa(userRepository.saveAndFlush(toJpa(user)));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserData findByUsername(String username) {
		return fromJpa(userRepository.findById(username).orElse(null));
	}

	@Override
	@Transactional(readOnly = true)
	public UserData findByUsernameSafe(String username) {
		return fromJpa(userRepository.findById(username).orElseThrow(UserNotFoundException::new));
	}

	@Override
	@Transactional(readOnly = false)
	public UserData updateUser(String username, UserData userRequest) {
		FriendsPreconditions.checkItsMe(username);

		Optional<User> existingUser = userRepository.findById(username);
		if (existingUser.isPresent()) {
			User updatedUser = existingUser.get();
			updatedUser.setVisible(userRequest.getVisible());
			if (Objects.nonNull(userRequest.getPassword()))
				updatedUser.setPassword(userRequest.getPassword());
			return fromJpa(userRepository.saveAndFlush(updatedUser));
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserData> findByVisibleIsTrue() {
		return fromJpa(userRepository.findByVisibleIsTrue());
	}

	@Override
	@Transactional(readOnly = true)
	public UserData findByUsernameAndVisibleIsTrue(String username) {
		return fromJpa(userRepository.findByUsernameAndVisibleIsTrue(username).orElseThrow(UserNotFoundException::new));
	}

	@Override
	@Transactional(readOnly = true)
	public UserData me() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(authentication))
			return findByUsername(authentication.getName());
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public void checkForNewBadges(String username) {
		User user = userRepository.findById(username).orElse(null);
		if (Objects.nonNull(user) && user.getDeclinesCount() > 2
				&& !user.getBadges().contains(BadgeEnum.FOREVER_ALONE)) {
			user.getBadges().add(BadgeEnum.FOREVER_ALONE);
			userRepository.saveAndFlush(user);
		}

	}
}
