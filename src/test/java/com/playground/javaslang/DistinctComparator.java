package com.playground.javaslang;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import io.vavr.collection.List;

public class DistinctComparator
{

	List<Item> items;

	@Before
	public void init()
	{
		items = List.of(new Item("apple", 10, new BigDecimal("1.99")),
				new Item("orange", 10, new BigDecimal("2.99")),
				new Item("orange", 10, new BigDecimal("2.99")),
				new Item("orange", 10, new BigDecimal("3.99")),
				new Item("papaya", 20, new BigDecimal("3.99")),
				new Item("grape", 10, new BigDecimal("5.99")),
				new Item("papaya", 20, new BigDecimal("3.99")), new Item(null, 20, null));
	}

	@Test
	public void useDistinctBy()
	{
		String distinct = items.filter(e -> e != null && e.getName() != null)
				.distinctBy(Item::getName)
				.map(Item::getName)
				.toJavaStream()
				.collect(Collectors.joining(", ", "Distinct: ", "."));
		System.out.println("Distinct By Function: " + distinct);
		
		distinct = items.filter(e -> e != null && e.getName() != null && e.getPrice() != null)
				.distinctBy(Comparator.comparing(Item::getName)
						.thenComparing(Item::getQty)
						.thenComparing(Item::getPrice))
				.map(Item::getName)
				.toJavaStream()
				.collect(Collectors.joining(", ", "Distinct: ", "."));
		System.out.println("Distinct By Comparator: " + distinct);
	}
}