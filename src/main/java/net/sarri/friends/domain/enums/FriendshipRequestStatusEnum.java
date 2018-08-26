package net.sarri.friends.domain.enums;

public enum FriendshipRequestStatusEnum {

	INITIATOR("The other user has to accept or decline the friend request"),
	RECEIVER("You have to accept or decline the friend request");

	private String description;

	private FriendshipRequestStatusEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name();
	}
}
