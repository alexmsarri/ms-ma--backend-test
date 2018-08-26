package net.sarri.friends.domain.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sarri.friends.domain.enums.BadgeEnum;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "badges", "friendshipInitiatorRequests", "friendshipReceiverRequests" })
@ToString(exclude = { "badges", "friendshipInitiatorRequests", "friendshipReceiverRequests" })
public class User implements Serializable {

	private static final long serialVersionUID = 3762680385970046353L;

	@Size(min = 5, max = 10)
	@Pattern(regexp = "^[a-zA-Z0-9]*", message = "Username only accepts alphanumeric characters")
	@Column(unique = true)
	@Id
	private String username;

	@Size(min = 8, max = 12)
	@Pattern(regexp = "^[a-zA-Z0-9]*", message = "Password only accepts alphanumeric characters")
	@Column
	private String password;

	@Column
	@NotNull
	private LocalDateTime dateJoined;

	@Column
	@NotNull
	private Boolean visible;

	@Column
	private int declinesCount;

	@Column
	private String[] roles = new String[] { "ROLE_USER" };

	@ElementCollection(targetClass = BadgeEnum.class)
	@Column
	@Enumerated(EnumType.STRING)
	private Set<BadgeEnum> badges = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "id.usernameInitiator")
	private Set<FriendshipRequest> friendshipInitiatorRequests = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "id.usernameReceiver")
	private Set<FriendshipRequest> friendshipReceiverRequests = new HashSet<>();

	@JoinColumn
	@ManyToMany
	private Set<Friendship> friendships = new HashSet<>();

}
