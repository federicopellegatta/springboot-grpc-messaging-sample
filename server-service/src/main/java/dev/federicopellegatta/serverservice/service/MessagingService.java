package dev.federicopellegatta.serverservice.service;

import com.google.protobuf.Timestamp;
import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.messaging.MessagingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

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
				.setReadTime(Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build())
				.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
