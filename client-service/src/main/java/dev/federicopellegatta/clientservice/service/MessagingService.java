package dev.federicopellegatta.clientservice.service;

import dev.federicopellegatta.clientservice.component.MessageMapper;
import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.clientservice.dto.MessagesBySenderResponse;
import dev.federicopellegatta.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import utils.TimeUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class MessagingService {
	private final ReactorMessagingServiceGrpc.ReactorMessagingServiceStub reactorMessagingServiceStub;
	private final MessageMapper messageMapper;
	
	public MessageClientResponse sendMessage(MessageClientRequest messageClientRequest) {
		MessageRequest serverRequest = messageMapper.toServerRequest(messageClientRequest);
		
		return reactorMessagingServiceStub.sendMessage(serverRequest)
				.map(messageMapper::toClientResponse)
				.block();
	}
	
	
	public MessagesBySenderResponse collectMessagesBySender(int numberOfSenders, int numberOfMessages) {
		List<Person> randomSenders = IntStream.range(0, numberOfSenders)
				.mapToObj(i -> new RandomGenerator().person())
				.collect(Collectors.toList());
		
		Flux<MessageRequest> messageRequestFlux = Flux.fromStream(
				IntStream.range(0, numberOfMessages)
						.mapToObj(i -> MessageRequest.newBuilder()
								.setSender(RandomGenerator.pickRandom(randomSenders))
								.setContent(new RandomGenerator().message())
								.setSendTime(TimeUtils.convertToTimestamp(Instant.now()))
								.build())
		);
		
		return reactorMessagingServiceStub.collectMessagesBySender(messageRequestFlux)
				.map(groupedMessagesResponse -> {
					List<MessagesBySenderResponse.SenderMessagesPairResponse> clientResponse =
							messageMapper.toClientResponse(groupedMessagesResponse.getSenderMessagesList());
					return new MessagesBySenderResponse(clientResponse);
				})
				.doOnError(error -> log.error("Error in collectMessagesBySender", error))
				.block();
	}
	
	public Collection<MessageClientResponse> sendMessageStream() {
		Flux<MessageRequest> messageRequestFlux = Flux.fromStream(
				IntStream.range(0, 100)
						.mapToObj(i -> MessageRequest.newBuilder()
								.setSender(new RandomGenerator().person())
								.setContent(new RandomGenerator().message())
								.setSendTime(TimeUtils.convertToTimestamp(Instant.now()))
								.build())
		);
		
		return reactorMessagingServiceStub.sendMessageStream(messageRequestFlux)
				.map(messageMapper::toClientResponse)
				.collectList()
				.doOnError(error -> log.error("Error in sendMessageStream", error))
				.block();
	}
}
