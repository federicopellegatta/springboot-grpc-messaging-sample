package dev.federicopellegatta.clientservice.service;

import dev.federicopellegatta.clientservice.component.MessageMapper;
import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.MessagingServiceGrpc;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessagingService {
	private final MessagingServiceGrpc.MessagingServiceBlockingStub messagingServiceBlockingStub;
	private final MessageMapper messageMapper;
	
	public MessageClientResponse sendMessage(MessageClientRequest messageClientRequest) {
		MessageRequest serverRequest = messageMapper.toServerRequest(messageClientRequest);
		
		MessageResponse serverResponse = messagingServiceBlockingStub.sendMessage(serverRequest);
		
		return messageMapper.toClientResponse(serverResponse);
	}
}
