package dev.federicopellegatta.serverservice.service;

import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.MessagingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import utils.TimeUtils;

import java.time.Instant;

@GrpcService
@Slf4j
public class MessagingService extends MessagingServiceGrpc.MessagingServiceImplBase {
	@Override
	public void sendMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
		Instant now = Instant.now();
		log.info("Received message from {}: {}", request.getSender(), request.getContent());
		
		MessageResponse response = MessageResponse.newBuilder()
				.setContent("Hi " + request.getSender() + ", I'm server!")
				.setRecipient(request.getSender())
				.setReadTime(TimeUtils.convertToTimestamp(now))
				.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
