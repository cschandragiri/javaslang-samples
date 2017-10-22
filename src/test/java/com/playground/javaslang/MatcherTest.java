package com.playground.javaslang;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static org.junit.Assert.assertEquals;
import io.vavr.control.Try;
import java.util.function.Function;

import org.junit.Test;

import io.vavr.Predicates;
import io.vavr.control.Option;

public class MatcherTest {

	@Test
	public void testMatchOf() {

		Function<Integer, String> converter = i ->
		{
			return Match(i).of(Case($(1), "one"), Case($(2), "two"), Case($(3), "three"), Case($(), "?"));
		};

		assertEquals("one", converter.apply(1));
		assertEquals("?", converter.apply(5));
	}

	@Test
	public void testMatchOption() {

		Function<Integer, Option<String>> converter = i ->
		{
			return Match(i).option(Case($(1), "one"), Case($(2), "two"), Case($(3), "three"));
		};

		assertEquals("Not Found", converter.apply(5).getOrElse("Not Found"));
		assertEquals("one", converter.apply(1).getOrElse("Not Found"));
	}

	@Test
	public void testMatchPredicate1() {

		Function<Integer, Option<String>> converter = i ->
		{
			return Match(i).option(
					Case($(Predicates.is(0)), "Zero"),
					Case($(Predicates.isNull()), "null"),
					Case($(Predicates.allOf(t -> t <= 10, Predicates.isIn(2, 4, 6, 8, 10))), "Even"),
					Case($(Predicates.allOf(t -> t <= 10, Predicates.isIn(1, 3, 5, 7, 9))), "Odd"));
		};
		assertEquals("Zero", converter.apply(0).getOrElse("Not Found"));
		assertEquals("Even", converter.apply(10).getOrElse("Not Found"));
		assertEquals("Odd", converter.apply(3).getOrElse("Not Found"));
		assertEquals("null", converter.apply(null).getOrElse("Not Found"));
		assertEquals("Not Found", converter.apply(-13).getOrElse("Not Found"));
	}

	@Test
	public void testMatchPredicate2() {

		Function<Integer, Option<String>> converter = i ->
		{
			return Match(i).option(
					Case($(Predicates.isNull()), "null"),
					Case($(n -> Math.abs(n) % 2 == 0), "Even"),
					Case($(n -> Math.abs(n) % 2 == 1), "Odd"));
		};
		assertEquals("Even", converter.apply(10).getOrElse("Not Found"));
		assertEquals("Odd", converter.apply(3).getOrElse("Not Found"));
		assertEquals("null", converter.apply(null).getOrElse("Not Found"));
		assertEquals("Odd", converter.apply(-13).getOrElse("Not Found"));
	}

	@Test
	public void testMatcherWithSideEffects() {
		Function<Integer, Try<String>> converter = i ->
		{
			return Match(i).of(
					Case($(Predicates.isNull()), Try.success("null")),
					Case($(n -> n > 0 && n % 2 == 0), Try.ofSupplier(() -> "Even")),
					Case($(n -> n > 0 && n % 2 == 1), Try.ofSupplier(() -> "Odd")),
					Case($(), o -> Try.failure(new IllegalArgumentException("Illegal Argument: " + o.toString()))));
		};
		assertEquals("Even", converter.apply(10).onFailure(v -> System.out.println(v)).getOrElse("Not Found"));
		assertEquals("Odd", converter.apply(3).onFailure(v -> System.out.println(v)).getOrElse("Not Found"));
		assertEquals("null", converter.apply(null).onFailure(v -> System.out.println(v)).getOrElse("Not Found"));
		assertEquals("Not Found", converter.apply(-10).onFailure(v -> System.out.println(v)).getOrElse("Not Found"));
	}
}
