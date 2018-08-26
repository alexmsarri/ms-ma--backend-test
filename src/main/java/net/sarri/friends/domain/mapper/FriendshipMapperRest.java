package net.sarri.friends.domain.mapper;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;

import net.sarri.friends.domain.business.FriendshipData;
import net.sarri.friends.domain.rest.response.FriendshipResponse;

public class FriendshipMapperRest {

	private FriendshipMapperRest() {
		super();
	}

	public static List<FriendshipResponse> fromBusiness(Collection<FriendshipData> userList) {
		return getMapper().map(userList, new TypeToken<Collection<FriendshipResponse>>() {
		}.getType());
	}

	@Bean
	public static ModelMapper getMapper() {
		return new ModelMapper();
	}

}
