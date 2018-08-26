package net.sarri.friends.domain.rest.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipResponse implements Serializable {

	private static final long serialVersionUID = -448439683490706052L;
	
	private String username;
	private LocalDateTime date;

}
