package com.playground.javaslang;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

/**
 * Split a Stream<Integer> into a Stream<Stream<Integer>> based on the same
 * number being repeated, only leaving the streams with odd numbers.
 * 
 * For example the following stream containing:
 * 
 * {1,1,1,2,2,2,3,6,7,7,1,1}
 * 
 * Would need to result in a stream of streams containing:
 * 
 * {{1,1,1},{2,2,2}, {3},{6}, {7,7},{1,1}}
 * 
 * @author cchandragiri
 *
 */
public class StreamSplitter {

	public Stream<Stream<Integer>> java8Style(int[] input) {
		Map<Integer, List<Integer>> grouped = Arrays.stream(input).boxed()
				.collect(Collectors.groupingBy(Function.identity()));
		return grouped.values().stream().map(List::stream);
	}

	public Stream<Stream<Integer>> streamEx(int[] input) {
		return IntStreamEx.of(input).boxed().groupRuns(Integer::equals).map(List::stream);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		StreamSplitter splitter = new StreamSplitter();
		int[] input = { 1, 1, 1, 2, 2, 2, 3, 6, 7, 7, 1, 1 };
		Stream<Stream<Integer>> java8 = splitter.java8Style(input);
		Stream<Stream<Integer>> streamEx = splitter.java8Style(input);
	}
}
