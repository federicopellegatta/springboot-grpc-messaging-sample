package dev.federicopellegatta.clientservice.component;

import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.clientservice.dto.PersonClient;
import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.Person;
import org.springframework.stereotype.Component;
import utils.TimeUtils;

import java.time.LocalDateTime;

@Component
public class MessageMapper {
	
	public MessageRequest toServerRequest(MessageClientRequest messageClientRequest) {
		return MessageRequest.newBuilder()
				.setContent(messageClientRequest.getContent())
				.setSender(toPersonServer(messageClientRequest.getSender()))
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
	
	private Person toPersonServer(PersonClient sender) {
		return Person.newBuilder()
				.setName(sender.getName())
				.setAge(sender.getAge())
				.setGender(sender.getGender())
				.build();
	}
}
