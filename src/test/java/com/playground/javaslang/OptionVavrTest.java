package com.playground.javaslang;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.vavr.collection.Stream;
import io.vavr.control.Option;
public class OptionVavrTest {

	@Test
	public void useDistinctBy()
	{
		final Option<Integer> option = Option.of(42);
        final Option<Number> narrow = Option.narrow(option);
        assertTrue(narrow.get().intValue() == 42);
        
        final Option<?> some = Option.some(null);
        assertTrue(!some.isEmpty());
        
        final Option<?> nullOption = Option.of(null);
        assertTrue(nullOption.isEmpty());
        
        Stream.from(1)               // 1, 2, 3, ...
        .filter(i -> i % 4 == 0) // 2, 4, 6, ...
        .sliding(2, 8)           // (2, 4), (6, 8), (10, 12), ...
        .take(2)                 // (2, 4), (6, 8)
        .forEach(s -> {
        		s.toStream().forEach(System.out::println);
        });
	}
}
