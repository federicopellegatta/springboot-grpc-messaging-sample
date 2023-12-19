package dev.federicopellegatta.clientservice.controller;

import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.clientservice.dto.MessagesBySenderResponse;
import dev.federicopellegatta.clientservice.service.MessagingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/messaging")
@AllArgsConstructor
public class MessagingController {
	private final MessagingService messagingService;
	
	@PostMapping(value = "/send")
	public ResponseEntity<MessageClientResponse> sendMessage(@RequestBody MessageClientRequest messageClientRequest) {
		return new ResponseEntity<>(messagingService.sendMessage(messageClientRequest), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/collect-messages-by-sender")
	public ResponseEntity<MessagesBySenderResponse> collectMessagesBySender(
			@RequestParam(value = "numberOfSenders", required = false, defaultValue = "3") int numberOfSenders,
			@RequestParam(value = "numberOfMessages", required = false, defaultValue = "5") int numberOfMessages) {
		return new ResponseEntity<>(messagingService.collectMessagesBySender(numberOfSenders, numberOfMessages),
		                            HttpStatus.CREATED);
	}
	
	@PostMapping("/send-to-all")
	public ResponseEntity<Collection<MessageClientResponse>> sendMessageToAll(
			@RequestParam(value = "numberOfRecipient", required = false, defaultValue = "3") int numberOfRecipient) {
		return new ResponseEntity<>(messagingService.sendMessageToAll(numberOfRecipient), HttpStatus.CREATED);
	}
	
	@PostMapping("/send-stream")
	public ResponseEntity<Collection<MessageClientResponse>> sendMessageStream() {
		return new ResponseEntity<>(messagingService.sendMessageStream(), HttpStatus.CREATED);
	}
}
