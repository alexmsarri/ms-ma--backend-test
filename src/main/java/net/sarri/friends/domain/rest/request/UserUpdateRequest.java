package net.sarri.friends.domain.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("User update")
@AllArgsConstructor
public class UserUpdateRequest implements Serializable {

	private static final long serialVersionUID = -4152983209717388193L;

	@NotNull
	@ApiModelProperty(value = "Visibility", required = true)
	private Boolean visible;

	@Size(min = 8, max = 12)
	@Pattern(regexp = "^[a-zA-Z0-9]*", message = "Password only accepts alphanumeric characters")
	@ApiModelProperty(value = "Password of the User (5-10 alphanumeric characters)", required = true)
	private String password;

}
