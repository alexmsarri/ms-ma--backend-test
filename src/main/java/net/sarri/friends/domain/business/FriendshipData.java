package net.sarri.friends.domain.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipData implements Serializable {

	private static final long serialVersionUID = -2967922612424286220L;

	private Long id;
	private LocalDateTime date;
	private Set<String> usernames = new HashSet<>();

}
