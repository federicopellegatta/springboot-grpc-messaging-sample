package dev.federicopellegatta.clientservice.component;

import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import org.springframework.stereotype.Component;
import utils.DateUtils;

import java.time.LocalDateTime;

@Component
public class MessageMapper {
	public MessageClientRequest toClientRequest(MessageRequest messageRequest) {
		return MessageClientRequest.builder()
				.content(messageRequest.getContent())
				.sender(messageRequest.getSender())
				.build();
	}
	
	public MessageRequest toServerRequest(MessageClientRequest messageClientRequest) {
		return MessageRequest.newBuilder()
				.setContent(messageClientRequest.getContent())
				.setSender(messageClientRequest.getSender())
				.setSendTime(DateUtils.convertToTimestamp(LocalDateTime.now()))
				.build();
	}
	
	public MessageClientResponse toClientResponse(MessageResponse messageResponse) {
		return MessageClientResponse.builder()
				.content(messageResponse.getContent())
				.recipient(messageResponse.getRecipient())
				.readTime(DateUtils.convertToLocalDateTime(messageResponse.getReadTime()))
				.build();
	}
	
	public MessageResponse toServerResponse(MessageClientResponse messageClientResponse) {
		return MessageResponse.newBuilder()
				.setContent(messageClientResponse.getContent())
				.setRecipient(messageClientResponse.getRecipient())
				.setReadTime(DateUtils.convertToTimestamp(messageClientResponse.getReadTime()))
				.build();
	}
}
