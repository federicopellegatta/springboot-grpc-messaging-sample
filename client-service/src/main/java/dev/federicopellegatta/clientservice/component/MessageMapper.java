package dev.federicopellegatta.clientservice.component;

import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import org.springframework.stereotype.Component;
import utils.TimeUtils;

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
				.setSendTime(TimeUtils.convertToTimestamp(LocalDateTime.now()))
				.build();
	}
	
	public MessageClientResponse toClientResponse(MessageResponse messageResponse) {
		return MessageClientResponse.builder()
				.content(messageResponse.getContent())
				.recipient(messageResponse.getRecipient())
				.readTime(TimeUtils.convertToLocalDateTime(messageResponse.getReadTime()))
				.build();
	}
	
	public MessageResponse toServerResponse(MessageClientResponse messageClientResponse) {
		return MessageResponse.newBuilder()
				.setContent(messageClientResponse.getContent())
				.setRecipient(messageClientResponse.getRecipient())
				.setReadTime(TimeUtils.convertToTimestamp(messageClientResponse.getReadTime()))
				.build();
	}
}
