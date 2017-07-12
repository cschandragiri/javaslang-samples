package com.playground.javaslang;

import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import io.vavr.control.Try;

public class TryRecoverExampleTest
{

	@Test
	public void useOptional()
	{
		// Using optional
		List<DayOfWeek> result = Stream
				.of("12/31/2014", "01-01-2015", "12/31/2015", "not a date", "01/01/2016")
				.map(TryRecoverExample::parseDateOptional)// Parse String to LocalDate
				.filter(Optional::isPresent) // Filter valid ones
				.map(Optional::get)// Get wrapped LocalDate
				.map(DayOfWeek::from) // Map to day of week
				.collect(Collectors.toList());
		Assert.assertNotNull(result);
		System.out.println("useOptional:" + result);
	}

	@Test
	public void tryLogError()
	{
		List<DayOfWeek> result = Stream
				.of("12/31/2014", "01-01-2015", "12/31/2015", "not a date", "01/01/2016")
				.map(TryRecoverExample::parseDateTry)// Parse String to LocalDate
				.peek(v -> v.onFailure(t -> System.out.println("Failed due to " + t.getMessage())))// Print
																									// error
																									// on
																									// failure
				.filter(Try::isSuccess)// Filter valids
				.map(Try::get)// Get wrapped value
				.map(DayOfWeek::from)// Map to day of week
				.collect(Collectors.toList());
		Assert.assertNotNull(result);
		System.out.println("tryLogError:" + result);
	}

	@Test
	public void tryRecover()
	{
		List<DayOfWeek> result = Stream
				.of("12/31/2014", "01-01-2015", "12/31/2015", "not a date", "01/01/2016")
				.map(TryRecoverExample::parseDateAlternate)// Parse String to LocalDate
				.map(v -> v.recoverWith(e -> TryRecoverExample
						.parseDateTry(((DateTimeParseException) e).getParsedString())))// Try
				// recovering
				// with
				// alternate
				// //
				// formatter
				.peek(v -> v.onFailure(t -> System.out.println("Failed due to " + t.getMessage())))// Print
																									// error
																									// on
																									// failure
				.filter(Try::isSuccess)// Filter valids
				.map(Try::get)// Get wrapped value
				.map(DayOfWeek::from)// Map to day of week
				.collect(Collectors.toList());
		Assert.assertNotNull(result);
		System.out.println("tryRecover: " +result);
	}
}
