package net.sarri.friends.domain.rest.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

	private static final long serialVersionUID = -6979072851247328211L;

	private String username;
	private LocalDateTime dateJoined;

	@JsonInclude(Include.NON_NULL)
	private Boolean visible;

	@JsonInclude(Include.NON_EMPTY)
	private List<BadgeResponse> badges;

}
