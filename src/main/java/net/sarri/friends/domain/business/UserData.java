package net.sarri.friends.domain.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import net.sarri.friends.domain.enums.BadgeEnum;

@Data
@Builder
public class UserData implements Serializable {

	private static final long serialVersionUID = -1029154630308674921L;

	private String username;
	private String password;
	private LocalDateTime dateJoined;
	private Boolean visible;
	private int declinesCount;

	private Set<BadgeEnum> badges = new HashSet<>();
	private Set<FriendshipRequestData> friendshipInitiatorRequests = new HashSet<>();
	private Set<FriendshipRequestData> friendshipReceiverRequests = new HashSet<>();
	private Set<FriendshipData> friendships = new HashSet<>();
	private Set<String> friends = new HashSet<>();
}
