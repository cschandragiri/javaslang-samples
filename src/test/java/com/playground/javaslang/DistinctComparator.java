package com.playground.javaslang;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.playground.javaslang.Item.ItemValidation;

import io.vavr.collection.List;

public class DistinctComparator {

	List<Item> items;

	@Before
	public void init() {
		items = List.of(
				new Item("apple", 10, new BigDecimal("1.99")),
				new Item("orange", 10, new BigDecimal("2.99")),
				new Item("orange", 10, new BigDecimal("2.99")),
				new Item("orange", 10, new BigDecimal("3.99")),
				new Item("papaya", 20, new BigDecimal("3.99")),
				new Item("grape", 10, new BigDecimal("5.99")),
				new Item("papaya", 20, new BigDecimal("3.99")),
				new Item(null, 20, null));
	}

	@Test
	public void useDistinctBy() {
		String distinct = items
				.filter(e -> e != null && e.getName() != null)
				.distinctBy(Item::getName)
				.map(Item::getName)
				.collect(Collectors.joining(", ", "Distinct: ", "."));
		System.out.println("Distinct By Function: " + distinct);

		distinct = items
				.filter(e -> e != null && e.getName() != null && e.getPrice() != null)
				.distinctBy(
						Comparator.comparing(Item::getName).thenComparing(Item::getQty).thenComparing(Item::getPrice))
				.map(Item::getName)
				.collect(Collectors.joining(", ", "Distinct: ", "."));
		System.out.println("Distinct By Comparator: " + distinct);

		distinct = items
				.toJavaStream()
				.filter(e -> e != null && e.getName() != null)
				.filter(distinctByKey(p -> p.getName()))
				.map(Item::getName)
				.collect(Collectors.joining(", ", "Distinct: ", "."));
		System.out.println("Distinct By Comparator: " + distinct);
	}

	@Test
	public void test() {

		String distinct = items
				.filter(e -> e != null && e.getName() != null)
				.filter(ItemValidation.nameIsNotEmpty().and(ItemValidation.eMailContainsAtSign())::apply)
				.map(Item::getName)
				.collect(Collectors.joining(", ", "Distinct: ", "."));
		System.out.println("Distinct By Function: " + distinct);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
