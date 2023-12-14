package dev.federicopellegatta.clientservice.service;

import com.github.javafaker.Faker;
import dev.federicopellegatta.messaging.Gender;
import dev.federicopellegatta.messaging.Person;

import java.util.List;
import java.util.Random;

public class RandomGenerator {
	private final Faker faker = new Faker();
	
	public Person person() {
		return Person.newBuilder()
				.setName(faker.name().name())
				.setAge(age())
				.setGender(gender())
				.build();
	}
	
	public String message() {
		return faker.lorem().sentence();
	}
	
	private Integer age() {
		return faker.number().numberBetween(18, 100);
	}
	
	private Gender gender() {
		return faker.bool().bool() ? Gender.MALE : Gender.FEMALE;
	}
	
	public static <T> T pickRandom(List<T> list) {
		return list.get(new Random().nextInt(list.size()));
	}
}
