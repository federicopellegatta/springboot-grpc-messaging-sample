package dev.federicopellegatta.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessagesBySenderResponse {
	private List<SenderMessagesPairResponse> senderMessagesPairs;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class SenderMessagesPairResponse {
		private PersonClient sender;
		private List<String> messages;
	}
}
