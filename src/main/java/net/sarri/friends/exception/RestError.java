package net.sarri.friends.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestError implements Serializable {

	private static final long serialVersionUID = 3119557088038377683L;
	
	private LocalDateTime timestamp;
	private String code;
	private String message;

}
