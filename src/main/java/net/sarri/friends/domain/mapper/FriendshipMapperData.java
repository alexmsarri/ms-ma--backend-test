package net.sarri.friends.domain.mapper;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;

import net.sarri.friends.domain.business.FriendshipData;
import net.sarri.friends.domain.jpa.Friendship;
import net.sarri.friends.domain.rest.response.FriendshipResponse;

public class FriendshipMapperData {

	private FriendshipMapperData() {
		super();
	}

	public static List<FriendshipData> fromJpa(Collection<Friendship> friendship) {
		return getMapper().map(friendship, new TypeToken<Collection<FriendshipResponse>>() {
		}.getType());
	}

	@Bean
	public static ModelMapper getMapper() {
		return new ModelMapper();
	}

}
