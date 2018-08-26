package net.sarri.friends.domain.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;

import net.sarri.friends.domain.business.UserData;
import net.sarri.friends.domain.jpa.User;

public class UserMapperData {
	private UserMapperData() {
		super();
	}

	public static User toJpa(UserData user) {
		if (user == null)
			return null;
		return getMapper().map(user, User.class);
	}

	public static UserData fromJpa(User user) {
		if (user == null)
			return null;
		UserData data = getMapper().map(user, UserData.class);
		Set<String> users = user.getFriendships().stream().flatMap(fr -> fr.getUsers().stream()).map(User::getUsername)
				.collect(Collectors.toSet());
		users.remove(user.getUsername());
		data.setFriends(users);
		return data;
	}

	public static List<UserData> fromJpa(List<User> users) {
		return getMapper().map(users, new TypeToken<List<UserData>>() {
		}.getType());
	}

	@Bean
	public static ModelMapper getMapper() {
		return new ModelMapper();
	}
}
