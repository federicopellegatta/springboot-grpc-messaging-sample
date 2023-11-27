package dev.federicopellegatta.clientservice.config;

import dev.federicopellegatta.messaging.MessagingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrcpClientConfig {
	
	@Bean
	ManagedChannel channel() {
		return ManagedChannelBuilder.forAddress("localhost", 9090)
				.usePlaintext()
				.build();
	}
	
	@Bean
	public MessagingServiceGrpc.MessagingServiceBlockingStub messagingServiceBlockingStub(ManagedChannel channel) {
		return MessagingServiceGrpc.newBlockingStub(channel);
	}
}
