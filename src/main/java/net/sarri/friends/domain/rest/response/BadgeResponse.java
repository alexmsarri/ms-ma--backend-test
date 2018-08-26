package net.sarri.friends.domain.rest.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadgeResponse implements Serializable {

	private static final long serialVersionUID = 2414282204105885030L;

	private String name;
	private String description;
	private String[] icon;

}
