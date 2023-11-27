package dev.federicopellegatta.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageClientResponse implements Serializable {
	private String recipient;
	private String content;
	private LocalDateTime readTime;
}
