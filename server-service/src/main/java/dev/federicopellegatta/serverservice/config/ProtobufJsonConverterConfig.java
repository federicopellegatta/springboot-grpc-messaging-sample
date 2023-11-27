package dev.federicopellegatta.serverservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

@Configuration
public class ProtobufJsonConverterConfig {
	@Bean
	public ProtobufJsonFormatHttpMessageConverter protobufHttpMessageConverter() {
		return new ProtobufJsonFormatHttpMessageConverter();
	}
}
