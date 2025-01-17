package com.dipl.abha.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {
	public static String convertToOffsetDateTimeWithMillis(String dateTimeStr) {
		// Parse the input string to LocalDateTime
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);

		// Add milliseconds to the LocalDateTime
		LocalDateTime dateTimeWithMillis = dateTime.withNano(0);

		// Convert to OffsetDateTime with UTC (Z)
		OffsetDateTime offsetDateTime = dateTimeWithMillis.atOffset(ZoneOffset.UTC);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

		return offsetDateTime.format(formatter);
	}

	public static void main(String[] args) {
		// Example input string
		String input = "2024-01-31T05:26:08";

		// Convert and print the result
		String result = convertToOffsetDateTimeWithMillis(input);

		System.out.println(result);
	}
}
