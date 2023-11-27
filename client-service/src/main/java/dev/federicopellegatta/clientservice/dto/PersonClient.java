package dev.federicopellegatta.clientservice.dto;

import dev.federicopellegatta.messaging.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PersonClient implements Serializable {
	private String name;
	private int age;
	private Gender gender;
}
