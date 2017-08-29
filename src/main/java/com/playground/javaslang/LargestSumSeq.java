package com.playground.javaslang;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

public class LargestSumSeq {

	public static void main(String[] args) {
		int[] input = { -2, -3, 4, -1, -2, 1, 5, -3, 9, -8, -3, 6, -11, 1, 9 };
		System.out.println("Maximum contiguous sum is " + StreamEx.of(maxContiguousSum(input)).joining(", "));

		System.out.println("Longest increasing number seq. is of size " + longestSeqLen(input).orElse(null));
		
		System.out.println("Longest increasing number seq. is " + StreamEx.of(longestSeq(input)).joining(", "));

	}

	private static Optional<Long> longestSeqLen(int[] input) {
		return IntStreamEx.of(input).boxed().collapse((a, b) -> b > a, Collectors.counting())
				.max(Comparator.naturalOrder());
	}
	
	private static List<Integer> longestSeq(int[] input) {
		return IntStreamEx.of(input).boxed().groupRuns((a, b) -> b > a)
				.maxBy(List::size).orElse(Collections.emptyList());
	}

	private static List<Integer> maxContiguousSum(int[] input) {
		List<Integer> longest = IntStreamEx.of(input).boxed().groupRuns((a, b) -> (a + b) > 1)
				.maxBy(l -> IntStreamEx.of(l).sum()).orElse(Collections.emptyList());
		return longest;
	}
}
