package com.playground.javaslang;

import static org.junit.Assert.assertEquals;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import io.vavr.collection.Stream;

public class VavrStreamTest {

	@Test
	public void sumOfDivisorsOf1_5Till1000() {
		Map<Integer, Number> result = Stream
				.rangeClosed(1, 1000)
				.groupBy(n -> n % 5)
				.toStream()
				.map((k) -> new AbstractMap.SimpleEntry<>(k._1(), k._2().sum()))
				.peek(e -> System.out.println(e.getKey() + ":" + e.getValue()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		assertEquals(result.values().stream().mapToInt(Number::intValue).sum(), IntStream.rangeClosed(1, 1000).sum());

	}

	@Test
	public void factorial() {
		int factorial = IntStream.rangeClosed(1, 5).reduce(1, (x, y) ->
		{
			return x * y;
		});
		assertEquals(120, factorial);

		int sum = IntStream.rangeClosed(1, 5).reduce((x, y) -> x + y).orElse(0);
		assertEquals(15, sum);
	}
}
