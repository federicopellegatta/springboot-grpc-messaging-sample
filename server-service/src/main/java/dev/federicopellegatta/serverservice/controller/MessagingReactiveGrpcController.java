package dev.federicopellegatta.serverservice.controller;

import dev.federicopellegatta.messaging.*;
import dev.federicopellegatta.serverservice.service.MessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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
	
	@Override
	public Mono<GroupedMessagesResponse> collectMessagesBySender(Flux<MessageRequest> request) {
		return request
				.groupBy(MessageRequest::getSender)
				.flatMap(groupedFlux -> groupedFlux.collectList()
						.map(messageRequests -> {
							List<String> messages = messageRequests
									.stream()
									.map(MessageRequest::getContent)
									.collect(Collectors.toList());
							
							return SenderMessagesPair.newBuilder()
									.setSender(groupedFlux.key())
									.addAllMessages(messages)
									.build();
						})
				)
				.collectList()
				.map(senderMessagesPairs -> GroupedMessagesResponse.newBuilder()
						.addAllSenderMessages(senderMessagesPairs)
						.build())
				.doOnError(error -> log.error("Error in collectMessagesBySender", error));
	}
}
