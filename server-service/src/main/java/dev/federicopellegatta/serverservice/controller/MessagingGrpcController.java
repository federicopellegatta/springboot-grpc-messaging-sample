package dev.federicopellegatta.serverservice.controller;

import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.MessagingServiceGrpc;
import dev.federicopellegatta.serverservice.service.MessagingService;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
@AllArgsConstructor
public class MessagingGrpcController extends MessagingServiceGrpc.MessagingServiceImplBase {
	private final MessagingService messagingService;
	
	@Override
	public void sendMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
		log.info("Received message from gRCP api");
		
		responseObserver.onNext(messagingService.sendMessage(request));
		responseObserver.onCompleted();
	}
}
