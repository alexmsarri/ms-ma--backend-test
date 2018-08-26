package net.sarri.friends.domain.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
public class Friendship implements Serializable {
	
	private static final long serialVersionUID = -5939035311942317043L;

	@NotNull
	@Column(unique = true)
	@GeneratedValue
	@Id
	private Long id;

	@Column
	private LocalDateTime date;

	@ManyToMany
	private Set<User> users = new HashSet<>();

}
