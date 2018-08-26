package net.sarri.friends.domain.rest.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequestResponse implements Serializable {

	private static final long serialVersionUID = 8381368876050410558L;
	
	private String username;
	private String status;
	private String message;

}
