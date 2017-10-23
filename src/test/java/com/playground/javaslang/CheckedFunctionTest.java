package com.playground.javaslang;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import io.vavr.CheckedFunction1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class CheckedFunctionTest {

	/**
	 * Returns the string equiv of poistive non null integers
	 * 
	 * @param integer
	 * @return
	 * @throws CustomCheckedException
	 */
	CheckedFunction1<Integer, String> intToStr = (integer) ->
	{
		if (integer != null && integer > 0) {
			return Integer.toString(integer);
		}
		throw new CustomCheckedException("Illegal Argument!");
	};

	@Test(expected = CustomCheckedException.class)
	public void testWithException() {
		List.of(21, null, 0, 2, -13).toStream().map(intToStr.unchecked()).collect(Collectors.toList());
	}

	@Test
	public void testCheckedFunction() {
		java.util.List<String> result = List
				.of(21, null, 0, 2, -13)
				.toStream()
				.map(CheckedFunction1.lift(intToStr))
				.filter(opt -> !opt.isEmpty())
				.map(Option::get)
				.collect(Collectors.toList());

		assertEquals(2, result.size());

		Map<Boolean, java.util.List<Option<String>>> map = List
				.of(21, null, 0, 2, -13)
				.toStream()
				.map(CheckedFunction1.lift(intToStr))
				.collect(Collectors.partitioningBy(Option::isEmpty));
		assertEquals(3, map.get(Boolean.TRUE).size());
		assertEquals(2, map.get(Boolean.FALSE).size());
	}

	@Test
	public void testLiftTry() {
		java.util.List<String> result = List
				.of(21, null, 0, 2, -13)
				.toStream()
				.map(CheckedFunction1.liftTry(intToStr))
				.peek(t ->
				{
					t.onFailure(System.out::println);
				})
				.filter(Try::isSuccess)
				.map(Try::get)
				.collect(Collectors.toList());

		assertEquals(2, result.size());
	}
}
