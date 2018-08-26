package net.sarri.friends.domain.rest.request;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("User registration")
public class UserRegisterRequest implements Serializable {

	private static final long serialVersionUID = -4058330656842306546L;

	@Size(min = 5, max = 10)
	@Pattern(regexp = "^[a-zA-Z0-9]*", message = "Username only accepts alphanumeric characters")
	@ApiModelProperty(value = "Username of the User (5-10 alphanumeric characters)", required = true)
	private String username;

	@Size(min = 8, max = 12)
	@Pattern(regexp = "^[a-zA-Z0-9]*", message = "Password only accepts alphanumeric characters")
	@ApiModelProperty(value = "Password of the User (8-12 alphanumeric characters)", required = true)
	private String password;
}
