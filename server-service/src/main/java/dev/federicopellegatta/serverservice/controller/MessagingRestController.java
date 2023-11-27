package dev.federicopellegatta.serverservice.controller;

import dev.federicopellegatta.messaging.MessageRequest;
import dev.federicopellegatta.messaging.MessageResponse;
import dev.federicopellegatta.serverservice.service.MessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messaging")
@AllArgsConstructor
@Slf4j
public class MessagingRestController {
	private final MessagingService messagingService;
	
	@PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
		log.info("Received message from rest api");
		
		return new ResponseEntity<>(messagingService.sendMessage(request), HttpStatus.CREATED);
	}
}
