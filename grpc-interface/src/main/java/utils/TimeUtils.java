package utils;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
	private TimeUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
		                               ZoneId.systemDefault());
	}
	
	public static Timestamp convertToTimestamp(LocalDateTime localDateTime) {
		return Timestamp.newBuilder()
				.setSeconds(localDateTime.atZone(java.time.ZoneId.systemDefault()).toEpochSecond())
				.setNanos(localDateTime.getNano())
				.build();
	}
	
	public static Timestamp convertToTimestamp(Instant instant) {
		return Timestamp.newBuilder()
				.setSeconds(instant.getEpochSecond())
				.setNanos(instant.getNano())
				.build();
	}
}
