package net.sarri.friends.domain.business;

import java.io.Serializable;

import lombok.Data;

@Data
public class FriendshipRequestData implements Serializable {

	private static final long serialVersionUID = -1270887564943026018L;

	private String userInitiator;
	private String userReceiver;

}
