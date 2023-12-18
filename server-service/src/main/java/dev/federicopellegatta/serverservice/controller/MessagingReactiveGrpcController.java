package dev.federicopellegatta.serverservice.controller;

import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.ReactorMessagingServiceGrpc;
import dev.federicopellegatta.serverservice.service.MessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
@Slf4j
@AllArgsConstructor
public class MessagingReactiveGrpcController extends ReactorMessagingServiceGrpc.MessagingServiceImplBase {
	private final MessagingService messagingService;
	
	@Override
	public Mono<MessageResponse> sendMessage(Mono<MessageRequest> request) {
		log.info("Received message from reactive gRCP api");
		
		return request.map(messagingService::sendMessage);
	}
}
