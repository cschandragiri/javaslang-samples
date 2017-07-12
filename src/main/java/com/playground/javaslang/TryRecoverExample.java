package com.playground.javaslang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import io.vavr.control.Try;

public class TryRecoverExample
{

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private static final DateTimeFormatter alternateFormatter = DateTimeFormatter
			.ofPattern("MM-dd-yyyy");

	/**
	 * Converts given date string in format "MM/dd/yyyy" to LocalDate.
	 */
	public static LocalDate parseDate(String dateString)
	{
		return LocalDate.from(formatter.parse(dateString));
	}

	public static Optional<LocalDate> parseDateOptional(String dateString)
	{
		LocalDate localDate = null;
		try
		{
			localDate = LocalDate.from(formatter.parse(dateString));
		}
		catch (DateTimeParseException e)
		{
		}
		return Optional.ofNullable(localDate);
	}

	public static Try<LocalDate> parseDateTry(String dateString)
	{
		Try<LocalDate> result = Try.of(() -> parseDate(dateString));
		return result;
	}

	public static Try<LocalDate> parseDateAlternate(String dateString)
	{
		return Try.of(() -> LocalDate.from(alternateFormatter.parse(dateString)));
	}
}
