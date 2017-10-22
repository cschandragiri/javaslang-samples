package com.playground.javaslang;

import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import io.vavr.control.Try;

public class TryRecoverExampleTest {

	@Test
	public void useOptional() {
		// Using optional
		List<DayOfWeek> result = Stream
				.of("12/31/2014", "01-01-2015", "12/31/2015", "not a date", "01/01/2016")
				.map(DateParser::parseDateOptional)// Parse String to LocalDate
				.filter(Optional::isPresent) // Filter valid ones
				.map(Optional::get)// Get wrapped LocalDate
				.map(DayOfWeek::from) // Map to day of week
				.collect(Collectors.toList());
		Assert.assertNotNull(result);
		System.out.println("useOptional:" + result);
	}

	@Test
	public void tryLogError() throws InterruptedException {
		List<DayOfWeek> result = Stream
				.of("12/31/2014", "01-01-2015", "12/31/2015", "not a date", "01/01/2016")
				.map(DateParser::parseDateTry)// Parse String to LocalDate
				// on error print message
				.peek(v -> v.onFailure(t -> System.out.println("Failed due to " + t.getMessage())))
				.filter(Try::isSuccess)// Filter valids
				.map(Try::get)// Get wrapped value
				.map(DayOfWeek::from)// Map to day of week
				.collect(Collectors.toList());
		Assert.assertNotNull(result);
		System.out.println("tryLogError:" + result);
	}

	@Test
	public void tryRecover() {
		List<DayOfWeek> result = Stream
				.of("12/31/2014", "01-01-2015", "12/31/2015", "not a date", "01/01/2016")
				.map(DateParser::parseDateAlternate)// Parse String to LocalDate
				// if error map to alternate convention
				.map(v -> v.recoverWith(e -> DateParser.parseDateTry(((DateTimeParseException) e).getParsedString())))// Try
				// on error print message
				.peek(v -> v.onFailure(t -> System.out.println("Failed due to " + t.getMessage())))
				.filter(Try::isSuccess)// Filter valids
				.map(Try::get)// Get wrapped value
				.map(DayOfWeek::from)// Map to day of week
				.collect(Collectors.toList());
		Assert.assertNotNull(result);
		System.out.println("tryRecover: " + result);
	}

	@Test
	public void tryRun() {
		AtomicInteger count = new AtomicInteger(0);
		Try.run(() -> count.compareAndSet(0, 1)).andFinallyTry(() -> count.getAndIncrement());
		Assert.assertEquals(count.get(), 2);
	}
}
