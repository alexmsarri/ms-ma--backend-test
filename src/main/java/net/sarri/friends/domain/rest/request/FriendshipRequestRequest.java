package net.sarri.friends.domain.rest.request;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("Friend Request")
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipRequestRequest implements Serializable {

	private static final long serialVersionUID = -3379755599008995019L;

	@Size(min = 5, max = 10)
	@Pattern(regexp = "^[a-zA-Z0-9]*", message = "Username only accepts alphanumeric characters")
	@ApiModelProperty(value = "Username of the User (5-10 alphanumeric characters)", required = true)
	private String username;

}
