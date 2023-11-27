package dev.federicopellegatta.serverservice.service;

import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import utils.TimeUtils;

import java.time.Instant;

@Service
@Slf4j
public class MessagingService {
	
	public MessageResponse sendMessage(MessageRequest request) {
		Instant now = Instant.now();
		log.info("Received message from {}: {}", request.getSender(), request.getContent());
		
		return MessageResponse.newBuilder()
				.setContent("Hi " + request.getSender() + ", I'm server!")
				.setRecipient(request.getSender())
				.setReadTime(TimeUtils.convertToTimestamp(now))
				.build();
	}
}
