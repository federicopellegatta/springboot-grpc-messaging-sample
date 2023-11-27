package dev.federicopellegatta.clientservice.service;

import com.google.protobuf.Timestamp;
import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.MessagingServiceGrpc;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class MessagingService {
	private final MessagingServiceGrpc.MessagingServiceBlockingStub messagingServiceBlockingStub;
	
	public MessageClientResponse sendMessage(MessageClientRequest messageClientRequest) {
		Instant now = Instant.now();
		MessageRequest serverRequest = MessageRequest.newBuilder()
				.setContent(messageClientRequest.getContent())
				.setSender(messageClientRequest.getSender())
				.setSendTime(Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build())
				.build();
		MessageResponse serverResponse = messagingServiceBlockingStub.sendMessage(serverRequest);
		
		LocalDateTime readTime = LocalDateTime.ofInstant(
				Instant.ofEpochSecond(serverResponse.getReadTime().getSeconds(),
				                      serverResponse.getReadTime().getNanos()),
				ZoneId.systemDefault()
		);
		
		return MessageClientResponse.builder()
				.recipient(serverResponse.getRecipient())
				.content(serverResponse.getContent())
				.readTime(readTime)
				.build();
	}
}
