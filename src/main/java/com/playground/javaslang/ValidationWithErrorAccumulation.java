package com.playground.javaslang;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.stream.Stream;

public class ValidationWithErrorAccumulation
{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	/**
	 * Parses given dates string in format "MM/dd/yyyy" to LocalDate and converts it to Period
	 * Returns Optional of Period if it is valid, otherwise Optional.empty
	 */
	private static Optional<Period> parseDateToPeriod(String dateString1, String dateString2)
	{
		LocalDate date1 = null, date2 = null;
		try
		{
			date1 = LocalDate.from(formatter.parse(dateString1));
			date2 = LocalDate.from(formatter.parse(dateString2));
		}
		catch (DateTimeParseException e)
		{
			System.out.println(e.getMessage());
		}
		return date1 != null && date2 != null ? Optional.of(Period.between(date1, date2))
				: Optional.empty();
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
		Stream.of("01/01/2015 , 12/31/2015", "01-01-2015 , 12-31-2015", "01/12/2014 , 01/01/2015",
				"01/01/2016 , 02/01/2016")
				.map(s -> s.split(" , ")) // Split pair of dates
				.map(v -> parseDateToPeriod(v[0], v[1])) // Parse them to Period
				.filter(Optional::isPresent) // Filter valids
				.map(Optional::get) // Get wrapped value
				.map(ValidationWithErrorAccumulation::toRelative) // Format to Relative String
																	// containing # of years,
																	// months, days
				.forEach(System.out::println);// Print
	}
}
