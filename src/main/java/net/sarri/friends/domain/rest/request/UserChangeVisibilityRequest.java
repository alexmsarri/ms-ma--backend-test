package net.sarri.friends.domain.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Change visibility to User")
public class UserChangeVisibilityRequest implements Serializable {

	private static final long serialVersionUID = 6395689715217841839L;

	@NotNull
	@ApiModelProperty(value = "Visible", required = true)
	private Boolean visible;

}
