package dev.federicopellegatta.clientservice.controller;

import dev.federicopellegatta.clientservice.dto.MessageClientRequest;
import dev.federicopellegatta.clientservice.dto.MessageClientResponse;
import dev.federicopellegatta.clientservice.service.MessagingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/messaging")
@AllArgsConstructor
public class MessagingController {
	private final MessagingService messagingService;
	
	@PostMapping("/send")
	public ResponseEntity<MessageClientResponse> sendMessage(@RequestBody MessageClientRequest messageClientRequest) {
		return new ResponseEntity<>(messagingService.sendMessage(messageClientRequest), HttpStatus.CREATED);
	}
	
	@PostMapping("/send-stream")
	public ResponseEntity<Collection<MessageClientResponse>> sendMessageStream() {
		return new ResponseEntity<>(messagingService.sendMessageStream(), HttpStatus.CREATED);
	}
}
