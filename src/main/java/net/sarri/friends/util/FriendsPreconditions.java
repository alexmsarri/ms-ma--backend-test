package net.sarri.friends.util;

import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.sarri.friends.exception.YouCantDoItToYourselfException;

public class FriendsPreconditions {

	private FriendsPreconditions() {
		super();
	}

	public static void checkItsMe(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(username) && Objects.nonNull(authentication)
				&& !authentication.getName().equals(username)) {
			throw new AccessDeniedException("Access is denied");
		}
	}

	public static void checkItsNotMe(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(username) && Objects.nonNull(authentication)
				&& authentication.getName().equals(username)) {
			throw new YouCantDoItToYourselfException();
		}
	}
}
