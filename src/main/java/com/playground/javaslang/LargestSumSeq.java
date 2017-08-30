package com.playground.javaslang;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

public class LargestSumSeq
{

	public static void main(String[] args)
	{
		int[] input =
		{
				-2, 1, -3, 4, -1, 2, 1, -5, 4, -3, 2, -1, 5, -4, 6, 6
		};
		System.out.println(
				"Longest increasing number seq. is of size " + longestSeqLen(input).orElse(null));

		System.out.println("Longest increasing number seq. is " + StreamEx.of(longestSeq(input))
				.joining(", "));

	}

	private static Optional<Long> longestSeqLen(int[] input)
	{
		return IntStreamEx.of(input)
				.boxed()
				.collapse((a, b) -> b >= a, Collectors.counting())
				.max(Comparator.naturalOrder());
	}

	private static List<Integer> longestSeq(int[] input)
	{
		return IntStreamEx.of(input)
				.boxed()
				.groupRuns((a, b) -> b >= a)
				.maxBy(List::size)
				.orElse(Collections.emptyList());
	}
}
