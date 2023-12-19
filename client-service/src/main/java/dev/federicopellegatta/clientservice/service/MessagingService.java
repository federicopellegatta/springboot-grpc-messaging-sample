package dev.federicopellegatta.clientservice.service;

import dev.federicopellegatta.clientservice.component.MessageMapper;
import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.clientservice.dto.MessagesBySenderResponse;
import dev.federicopellegatta.messaging.*;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import utils.TimeUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class MessagingService {
	private final MessagingServiceGrpc.MessagingServiceBlockingStub messagingServiceBlockingStub;
	private final MessagingServiceGrpc.MessagingServiceStub messagingServiceStub;
	private final MessageMapper messageMapper;
	
	public MessageClientResponse sendMessage(MessageClientRequest messageClientRequest) {
		MessageRequest serverRequest = messageMapper.toServerRequest(messageClientRequest);
		
		MessageResponse serverResponse = messagingServiceBlockingStub.sendMessage(serverRequest);
		
		return messageMapper.toClientResponse(serverResponse);
	}
	
	
	public MessagesBySenderResponse collectMessagesBySender(int numberOfSenders, int numberOfMessages) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		MessagesBySenderResponse messagesBySenderResponse = new MessagesBySenderResponse();
		
		StreamObserver<MessageRequest> messageRequestStreamObserver =
				messagingServiceStub.collectMessagesBySender(new StreamObserver<>() {
					@Override
					public void onNext(GroupedMessagesResponse groupedMessagesResponse) {
						messagesBySenderResponse.setSenderMessagesPairs(
								messageMapper.toClientResponse(groupedMessagesResponse.getSenderMessagesList()));
					}
					
					@Override
					public void onError(Throwable throwable) {
						log.error("Error in collectMessagesBySender", throwable);
						countDownLatch.countDown();
					}
					
					@Override
					public void onCompleted() {
						countDownLatch.countDown();
					}
				});
		
		List<Person> randomSenders = IntStream.range(0, numberOfSenders)
				.mapToObj(i -> new RandomGenerator().person())
				.collect(Collectors.toList());
		
		IntStream.range(0, numberOfMessages)
				.mapToObj(i -> MessageRequest.newBuilder()
						.setSender(RandomGenerator.pickRandom(randomSenders))
						.setContent(new RandomGenerator().message())
						.setSendTime(TimeUtils.convertToTimestamp(Instant.now()))
						.build())
				.forEach(messageRequestStreamObserver::onNext);
		messageRequestStreamObserver.onCompleted();
		
		boolean isCountdownComplete = false;
		try {
			isCountdownComplete = countDownLatch.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Countdown was interrupted: " + e.getMessage());
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
		return isCountdownComplete ? messagesBySenderResponse : new MessagesBySenderResponse();
	}
	
	
	public Collection<MessageClientResponse> sendMessageToAll(int numberOfRecipient) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		final List<MessageClientResponse> messageClientResponses = new ArrayList<>();
		
		StreamObserver<MessageResponse> messageResponseStreamObserver =
				new StreamObserver<>() {
					@Override
					public void onNext(MessageResponse response) {
						messageClientResponses.add(messageMapper.toClientResponse(response));
					}
					
					@Override
					public void onError(Throwable throwable) {
						log.error("Error in sendMessageToAll", throwable);
						countDownLatch.countDown();
					}
					
					@Override
					public void onCompleted() {
						countDownLatch.countDown();
					}
				};
		
		RecipientsRequest recipientsRequest = RecipientsRequest.newBuilder()
				.addAllRecipients(IntStream.range(0, numberOfRecipient)
						                  .mapToObj(i -> new RandomGenerator().person())
						                  .collect(Collectors.toList()))
				.build();
		
		messagingServiceStub.sendMessageToAll(recipientsRequest, messageResponseStreamObserver);
		
		boolean isCountdownComplete = false;
		try {
			isCountdownComplete = countDownLatch.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Countdown was interrupted: " + e.getMessage());
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
		return isCountdownComplete ? messageClientResponses : Collections.emptyList();
	}
	
	public Collection<MessageClientResponse> sendMessageStream() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		List<MessageClientResponse> messageClientResponses = new ArrayList<>();
		
		StreamObserver<MessageRequest> messageRequestStreamObserver = messagingServiceStub.sendMessageStream(
				new StreamObserver<>() {
					@Override
					public void onNext(MessageResponse messageResponse) {
						messageClientResponses.add(messageMapper.toClientResponse(messageResponse));
					}
					
					@Override
					public void onError(Throwable throwable) {
						log.error("Error in sendMessageStream", throwable);
						countDownLatch.countDown(); // Notify the countDownLatch that the stream is complete
					}
					
					@Override
					public void onCompleted() {
						countDownLatch.countDown(); // Notify the countDownLatch that the stream is complete
					}
				}
		);
		
		IntStream.range(0, 100)
				.mapToObj(i -> MessageRequest.newBuilder()
						.setSender(Person.newBuilder().setName("Client").setAge(30).setGender(Gender.FEMALE).build())
						.setContent("This is the message number " + i + " from client")
						.setSendTime(TimeUtils.convertToTimestamp(Instant.now()))
						.build())
				.forEach(messageRequestStreamObserver::onNext);
		messageRequestStreamObserver.onCompleted();
		
		boolean isCountdownComplete = false;
		try {
			isCountdownComplete = countDownLatch.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Countdown was interrupted: " + e.getMessage());
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}
		return isCountdownComplete ? messageClientResponses : Collections.emptyList();
	}
}
