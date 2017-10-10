package com.playground.javaslang;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;

public class ValidationWithErrorAccumulationVavr
{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	/**
	 * Parses given tuple of two dates to Period and accumulates errors. Returns Validation of
	 * Period if both dates are valid, otherwise Validation of list of error messages.
	 */
	private static Validation<Seq<String>, Period> parseDateToPeriod(Tuple2<String, String> dates)
	{
		return parseDate(dates._1()).combine(parseDate(dates._2()))
				.ap((date1, date2) -> Period.between(date1, date2));
	}

	/**
	 * Parses given date string in format "MM/dd/yyyy" to LocalDate Returns Validation of LocalDate
	 * if it is valid, otherwise Validation of String containing error message.
	 */
	private static Validation<String, LocalDate> parseDate(String dateString)
	{
		Try<LocalDate> parsedDate = Try.of(() -> LocalDate.from(formatter.parse(dateString)));
		return parsedDate.isSuccess() ? Validation.valid(parsedDate.get())
				: Validation.invalid(parsedDate.getCause()
						.getMessage());
	}

	/**
	 * Formats given Period as String representing number of years, months and days.
	 */
	private static String toRelative(Period p)
	{
		return String.format("%s years , %s months and %s days", p.getYears(), p.getMonths(),
				p.getDays());
	}

	public static void main(String args[])
	{
		Stream.of(Tuple.of("01/01/2015", "12/31/2015"), Tuple.of("01-01-2015", "12-31-2015"),
				Tuple.of("01/12/2014", "01/01/2015"), Tuple.of("01/01/2015", "01/01/2016"))
				.map(ValidationWithErrorAccumulationVavr::parseDateToPeriod) // Parse dates to Period
				.peek(v ->
				{
					if (v.isInvalid())
						System.out.println(v.getError());
				})// Print errors for invalid ones
				.filter(Validation::isValid) // Filter valids
				.map(Validation::get) // Get wrapped Period
				.map(ValidationWithErrorAccumulationVavr::toRelative) // Format to Relative String
																	// containing # of years,
																	// months, days
				.forEach(System.out::println);// Print
	}
}
