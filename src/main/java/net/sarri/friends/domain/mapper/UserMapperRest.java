package net.sarri.friends.domain.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;

import net.sarri.friends.domain.business.UserData;
import net.sarri.friends.domain.rest.request.UserChangeVisibilityRequest;
import net.sarri.friends.domain.rest.request.UserRegisterRequest;
import net.sarri.friends.domain.rest.request.UserUpdateRequest;
import net.sarri.friends.domain.rest.response.UserResponse;

public class UserMapperRest {

	private UserMapperRest() {
		super();
	}

	public static UserData toBusiness(UserRegisterRequest user) {
		return getMapper().map(user, UserData.class);
	}

	public static UserData toBusiness(UserChangeVisibilityRequest user) {
		return getMapper().map(user, UserData.class);
	}

	public static UserData toBusiness(UserUpdateRequest user) {
		return getMapper().map(user, UserData.class);
	}

	public static List<UserResponse> fromBusiness(List<UserData> userList) {
		return getMapper().map(userList, new TypeToken<List<UserResponse>>() {
		}.getType());
	}

	public static UserResponse fromBusiness(UserData user) {
		return fromBusiness(user, true);
	}

	public static UserResponse fromBusiness(UserData user, boolean complete) {
		if (complete)
			return getMapper().map(user, UserResponse.class);
		else
			return getFilteredMapper().map(user, UserResponse.class);
	}

	@Bean
	public static ModelMapper getMapper() {
		return new ModelMapper();
	}

	@Bean
	public static ModelMapper getFilteredMapper() {
		ModelMapper filteredMapper = new ModelMapper();
		TypeMap<UserData, UserResponse> filteredType = filteredMapper.createTypeMap(UserData.class, UserResponse.class);
		filteredType.addMappings(mapper -> mapper.skip(UserResponse::setVisible));

		return filteredMapper;
	}

}
