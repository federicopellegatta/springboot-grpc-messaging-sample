package dev.federicopellegatta.serverservice.controller;

import dev.federicopellegatta.messaging.*;
import dev.federicopellegatta.serverservice.service.MessagingService;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	@Override
	public StreamObserver<MessageRequest> collectMessagesBySender(
			StreamObserver<GroupedMessagesResponse> responseObserver) {
		return new StreamObserver<>() {
			final List<SenderMessagesPair> senderMessagesPairs = new ArrayList<>();
			
			@Override
			public void onNext(MessageRequest request) {
				Optional<SenderMessagesPair> matchedPairOptional = senderMessagesPairs.stream()
						.filter(pair -> pair.getSender().equals(request.getSender()))
						.findFirst();
				
				List<String> messages = new ArrayList<>(List.of(request.getContent()));
				if (matchedPairOptional.isPresent()) {
					SenderMessagesPair matchedPair = matchedPairOptional.get();
					senderMessagesPairs.remove(matchedPair);
					messages.addAll(matchedPair.getMessagesList());
				}
				
				senderMessagesPairs.add(SenderMessagesPair.newBuilder()
						                        .setSender(request.getSender())
						                        .addAllMessages(messages)
						                        .build());
			}
			
			@Override
			public void onError(Throwable throwable) {
				log.error("Error in collectMessagesBySender", throwable);
				responseObserver.onError(throwable);
			}
			
			@Override
			public void onCompleted() {
				responseObserver.onNext(GroupedMessagesResponse.newBuilder()
						                        .addAllSenderMessages(senderMessagesPairs)
						                        .build());
				responseObserver.onCompleted();
			}
		};
	}
	
	@Override
	public StreamObserver<MessageRequest> sendMessageStream(StreamObserver<MessageResponse> responseObserver) {
		List<MessageResponse> messageResponses = new ArrayList<>();
		
		return new StreamObserver<>() {
			@Override
			public void onNext(MessageRequest request) {
				messageResponses.add(messagingService.sendMessage(request));
			}
			
			@Override
			public void onError(Throwable throwable) {
				log.error("Error in sendMessageStream", throwable);
				responseObserver.onError(throwable);
			}
			
			@Override
			public void onCompleted() {
				messageResponses.forEach(responseObserver::onNext);
				responseObserver.onCompleted();
			}
		};
	}
}
