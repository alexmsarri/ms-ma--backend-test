package net.sarri.friends.domain.jpa;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = { "userInitiator", "userReceiver" })
@EqualsAndHashCode(exclude = { "userInitiator", "userReceiver" })
public class FriendshipRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	FriendshipRequestId id;

	@ManyToOne
	@MapsId("usernameInitiator")
	private User userInitiator;

	@ManyToOne
	@MapsId("usernameReceiver")
	private User userReceiver;

	public FriendshipRequest(User initiator, User receiver) {
		this.userInitiator = initiator;
		this.userReceiver = receiver;

		this.id = new FriendshipRequestId(initiator.getUsername(), receiver.getUsername());

	}

}
