package net.sarri.friends.domain.mapper;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;

import net.sarri.friends.domain.enums.FriendshipRequestStatusEnum;
import net.sarri.friends.domain.jpa.FriendshipRequest;
import net.sarri.friends.domain.rest.response.FriendshipRequestResponse;

public class FriendshipRequestMapperRest implements BiFunction<FriendshipRequest, String, FriendshipRequestResponse> {

	@Override
	public FriendshipRequestResponse apply(FriendshipRequest input, String username) {
		FriendshipRequestResponse output = new FriendshipRequestResponse();
		if (input.getUserReceiver().getUsername().equals(username)) {
			output.setStatus(FriendshipRequestStatusEnum.RECEIVER.name());
			output.setUsername(input.getUserInitiator().getUsername());
			output.setMessage(FriendshipRequestStatusEnum.RECEIVER.getDescription());
		} else if (input.getUserInitiator().getUsername().equals(username)) {
			output.setStatus(FriendshipRequestStatusEnum.INITIATOR.name());
			output.setUsername(input.getUserReceiver().getUsername());
			output.setMessage(FriendshipRequestStatusEnum.INITIATOR.getDescription());
		}
		return output;
	}

	public static List<FriendshipRequestResponse> toResponse(List<FriendshipRequest> requests, String username) {
		return requests.stream().map(request -> getMapper().apply(request, username)).collect(Collectors.toList());
	}

	public static FriendshipRequestResponse toResponse(FriendshipRequest request, String username) {
		return getMapper().apply(request, username);
	}

	@Bean
	public static FriendshipRequestMapperRest getMapper() {
		return new FriendshipRequestMapperRest();
	}

}
