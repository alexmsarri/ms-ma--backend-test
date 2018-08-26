package net.sarri.friends.domain.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipRequestId implements Serializable {

	private static final long serialVersionUID = -4365111735405460196L;

	private String usernameInitiator;

	private String usernameReceiver;

}
