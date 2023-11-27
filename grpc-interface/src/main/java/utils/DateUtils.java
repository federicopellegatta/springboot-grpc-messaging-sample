package utils;

import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;

public class DateUtils {
	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
		return LocalDateTime.ofInstant(
				java.time.Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
				java.time.ZoneId.systemDefault()
		);
	}
	
	public static Timestamp convertToTimestamp(LocalDateTime localDateTime) {
		return Timestamp.newBuilder()
				.setSeconds(localDateTime.atZone(java.time.ZoneId.systemDefault()).toEpochSecond())
				.setNanos(localDateTime.getNano())
				.build();
	}
}
